package com.odong.core.json.impl;

import com.odong.core.json.JsonHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-30上午2:28.
 */
public class JsonFastjsonHelperImpl implements JsonHelper {
    @Override
    public String object2json(Object object) {
        return null;
    }

    @Override
    public <T> T json2object(String json, Class<T> clazz) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> json2map(String json, Class<K> kClazz, Class<V> vClazz) {
        return null;
    }

    @Override
    public <T> List<T> json2List(String json, Class<T> clazz) {
        return null;
    }

    @Override
    public void init(){

    }
}
