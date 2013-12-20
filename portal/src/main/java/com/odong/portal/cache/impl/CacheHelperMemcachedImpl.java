package com.odong.portal.cache.impl;

import com.odong.portal.cache.CacheHelper;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:28
 */
public class CacheHelperMemcachedImpl implements CacheHelper {
    @Override
    public Map<InetSocketAddress, Map<String, String>> status() {
        try {
            return client.getStats();  //
        } catch (MemcachedException | InterruptedException | TimeoutException e) {
            logger.error("获取状态出错", e);
        }
        return new HashMap<>();
    }

    @Override
    public void delete(String key) {
        try {
            logger.debug("DELETE " + key);
            client.delete(key(key));
        } catch (MemcachedException | TimeoutException | InterruptedException e) {
            logger.error("删除缓存[{}]出错", key, e);

        }

    }

    @Override
    public void touch(String key, int timeout) {
        try {
            logger.debug("TOUCH " + key);
            client.touch(key(key), timeout);
        } catch (MemcachedException | TimeoutException | InterruptedException e) {
            logger.error("延长缓存有效期[{}]出错", key, e);

        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            logger.debug("GET " + key);
            return client.get(key(key));
        } catch (MemcachedException | TimeoutException | InterruptedException e) {
            logger.error("取得缓存[{}]出错", key, e);
        }
        return null;
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
        return t;  //
    }

    @Override
    public void set(String key, int timeout, Object object) {
        try {
            if (object == null) {
                logger.error("空的缓存内容[{}]", key);
                return;
            }
            client.set(key(key), timeout, object);
            logger.debug("SET " + key);
        } catch (MemcachedException | TimeoutException | InterruptedException e) {
            logger.error("取得缓存[{}]出错", key, e);

        }
    }

    private String key(String key) {
        return "cache://" + appName + "/" + key;
    }


    private MemcachedClient client;
    private String appName;
    private final static Logger logger = LoggerFactory.getLogger(CacheHelperMemcachedImpl.class);

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

}
