package com.odong.core.cache.impl;

import com.odong.core.cache.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-30上午2:26.
 */
public class CacheMemcachedHelperImpl implements CacheHelper {
    @Override
    public void delete(String key) {

    }

    @Override
    public void touch(String key, int timeout) {

    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz, Integer timeout, Callback<T> callback) {
        return null;
    }

    @Override
    public void set(String key, int timeout, Object object) {

    }
    private final static Logger logger = LoggerFactory.getLogger(CacheMemcachedHelperImpl.class);
}
