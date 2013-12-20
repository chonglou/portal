package com.odong.portal.cache.impl;

import com.odong.portal.cache.CacheHelper;
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
public class CacheHelperRedisImpl implements CacheHelper {
    @Override
    public Map<InetSocketAddress, Map<String, String>> status() {
        return new HashMap<>();
    }

    @Override
    public void delete(String key) {
        Jedis client = pool.getResource();
        try {
            client.del(key(key));
        } finally {
            pool.returnResource(client);
        }
    }

    @Override
    public void touch(String key, int timeout) {
        Jedis client = pool.getResource();
        try {
            client.expire(key(key), timeout);
        } finally {
            pool.returnResource(client);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Jedis client = pool.getResource();
        try {
            byte[] buf = client.get(key(key));
            return buf == null ? null : (T) bin2obj(buf);
        } finally {
            pool.returnResource(client);
        }
    }


    @Override
    public <T> T get(String key, Class<T> clazz, Integer timeout, Callback<T> callback) {
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
            byte[] k = key(key);

            client.set(k, obj2bin(object));
            client.expire(k, timeout);
        } finally {
            pool.returnResource(client);
        }
    }

    void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(100);
        config.setMaxIdle(20);
        config.setMaxWait(10001);
        pool = new JedisPool(config, host, port);
    }

    void destroy() {
        pool.destroy();
    }

    private byte[] key(String key) {
        return ("cache://" + appName + "/" + key).getBytes();
    }

    private byte[] obj2bin(Object obj) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("序列化对象出错", e);
        }
        return null;
    }

    private Object bin2obj(byte[] bytes) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("反序列化对象出错", e);
        }
        return null;
    }

    private JedisPool pool;
    private String host;
    private int port;
    private int maxConn;
    private String appName;
    private final static Logger logger = LoggerFactory.getLogger(CacheHelperRedisImpl.class);

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
