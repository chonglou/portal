package com.odong.core.json;

import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-30上午2:27.
 */
public interface JsonHelper {

    String object2json(Object object);

    <T> T json2object(String json, Class<T> clazz);

    <K, V> Map<K, V> json2map(String json, Class<K> kClazz, Class<V> vClazz);

    <T> List<T> json2List(String json, Class<T> clazz);
}
