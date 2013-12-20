package com.odong.portal.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;

/**
 * Created by flamen on 13-12-19下午10:36.
 */
public abstract class RedisHelper {
    protected abstract JedisPool getPool();

    protected abstract String getAppName();

    protected abstract String getPrefix();

    protected Object execute(Callback callback) {
        Jedis client = getPool().getResource();
        try {
            return callback.execute(client);
        } finally {
            getPool().returnResource(client);
        }
    }


    protected String id2key(byte[] id) {
        return new String(id).substring(prefix().length());
    }

    protected byte[] key2id(String key) {
        return (prefix() + key).getBytes();
    }

    private String prefix() {
        return getAppName() + "://" + getPrefix() + "/";
    }

    protected byte[] obj2bin(Object obj) {
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

    protected Object bin2obj(byte[] bytes) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("反序列化对象出错", e);
        }
        return null;
    }

    public interface Callback {
        Object execute(Jedis client);
    }

    private final static Logger logger = LoggerFactory.getLogger(RedisHelper.class);

}
