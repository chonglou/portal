package com.odong.portal.util;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:28
 */
public interface CacheHelper {
    Map<InetSocketAddress, Map<String,String>> status();
    void delete(String key);

    void touch(String key, int timeout);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, Class<T> clazz, Integer timeout, Callback<T> callback);

    void set(String key, int timeout, Object object);

    public interface Callback<T>{
        T call();
    }
}
