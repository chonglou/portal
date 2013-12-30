package com.odong.core.cache.impl;

import com.odong.core.cache.CacheHelper;
import com.odong.core.cache.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-30上午2:26.
 */
public class CacheRedisHelperImpl extends RedisHelper implements CacheHelper{
    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public Map<String, Map<String, String>> status() {
        return new HashMap<>();
    }

    @Override
    public void delete(String key) {
        execute((Jedis client) -> client.del(key2id(key)));

    }

    @Override
    public void touch(String key, int timeout) {

        execute((Jedis client) -> client.expire(key2id(key), timeout));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T get(String key, Class<T> clazz) {

        return (T) execute((Jedis client) -> {
            byte[] buf = client.get(key2id(key));
            return buf == null ? null : bin2obj(buf);
        });
    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz, Integer timeout, CacheHelper.Callback<T> callback) {
        T t = get(key, clazz);
        if (t == null) {
            t = callback.call();
            if (timeout == null || timeout <= 0) {
                timeout = 60 * 60 * 24;
            }
            set(key, timeout, t);
        }
        return t;
    }

    @Override
    public void set(String key, int timeout, Object object) {
        Jedis client = pool.getResource();
        try {
            byte[] k = key2id(key);

            client.set(k, obj2bin(object));
            client.expire(k, timeout);
        } finally {
            pool.returnResource(client);
        }
    }

    @Override
    protected JedisPool getPool() {
        return pool;
    }

    @Override
    protected String getAppName() {
        return appName;
    }

    @Override
    protected String getPrefix() {
        return "cache";
    }

    private JedisPool pool;
    private String appName;
 private final static Logger logger = LoggerFactory.getLogger(CacheRedisHelperImpl.class);

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
