package com.odong.portal.service;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
public interface SiteService {

    void setObject(String key, Object value);

    <T> T getObject(String key, Class<T> clazz);
}
