package com.odong.portal.util;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:27
 */
public interface JsonHelper {
    String object2json(Object object);

    <T> T json2object(String json, Class<T> clazz);

    <K, V> Map<K, V> json2map(String json, Class<K> kClazz, Class<V> vClazz);

    <T> List<T> json2List(String json, Class<T> clazz);

}
