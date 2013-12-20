package com.odong.portal.cache.impl;

import com.odong.portal.cache.CacheHelper;
import com.odong.portal.redis.RedisHelper;
import com.odong.portal.redis.RedisPool;
import com.odong.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-19.
 */
public class CacheHelperRedisImpl extends RedisHelper implements CacheHelper {
    @Override
    public Map<InetSocketAddress, Map<String, String>> status() {
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
    public <T> T get(String key, Class<T> clazz) {
        return (T) execute((Jedis client) -> {
            byte[] buf = client.get(key2id(key));
            return buf == null ? null : bin2obj(buf);
        });
    }


    @Override
    public <T> T get(String key, Class<T> clazz, Integer timeout, CacheHelper.Callback<T> callback) {
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
    private final static Logger logger = LoggerFactory.getLogger(CacheHelperRedisImpl.class);

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


}
