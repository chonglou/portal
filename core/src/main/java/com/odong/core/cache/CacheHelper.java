package com.odong.core.cache;

import java.util.Map;

/**
 * Created by flamen on 13-12-30上午2:25.
 */
public interface CacheHelper {
    void destroy();

    void init();

    Map<String, Map<String, String>> status();

    void delete(String key);

    void touch(String key, int timeout);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, Class<T> clazz, Integer timeout, Callback<T> callback);

    void set(String key, int timeout, Object object);

    public interface Callback<T> {
        T call();
    }
}