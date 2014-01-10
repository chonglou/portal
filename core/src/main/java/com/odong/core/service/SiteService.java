package com.odong.core.service;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-30下午3:50.
 */
public interface SiteService {

    void pop(String key);

    void set(String key, Object obj);


    void set(String key, Object obj, boolean encrypt);

    <T extends Serializable> T get(String key, Class<T> clazz);

    <T extends Serializable> T get(String key, Class<T> clazz, boolean encrypt);
}
