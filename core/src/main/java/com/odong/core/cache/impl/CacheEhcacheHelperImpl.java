package com.odong.core.cache.impl;

import com.odong.core.cache.CacheHelper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-30下午12:55.
 */
public final class CacheEhcacheHelperImpl implements CacheHelper {
    @Override
    public void destroy() {
        manager.shutdown();
    }

    @Override
    public void init() {
        manager = CacheManager.newInstance(getClass().getResource("/ehcache.xml"));
        manager.addCache(appName);
    }

    @Override
    public Map<String, Map<String, String>> status() {
        Map<String, Map<String, String>> status = new HashMap<>();
        Cache cache = getCache();
        Map<String, String> map = new HashMap<>();
        map.put("size", "" + cache.getSize());
        map.put("inMemory", "" + cache.getMemoryStoreSize());
        map.put("inDisk", "" + cache.getDiskStoreSize());
        status.put(cache.getName(), map);

        return status;
    }

    @Override
    public void delete(String key) {
        getCache().remove(key(key));
    }

    @Override
    public void touch(String key, int timeout) {
        logger.error("暂不支持ehcache的touch操作");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Element e = getCache().get(key(key));
        return e == null ? null : (T) e.getObjectValue();
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
        Element el = new Element(key(key), object);
        getCache().put(el);
    }

    private String key(String key) {
        return "cache://" + key;
    }

    private Cache getCache() {
        return manager.getCache(appName);
    }

    private CacheManager manager;
    private String appName;
    private final static Logger logger = LoggerFactory.getLogger(CacheEhcacheHelperImpl.class);

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
