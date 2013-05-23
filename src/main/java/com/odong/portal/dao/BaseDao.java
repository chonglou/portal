package com.odong.portal.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:30
 */
public interface BaseDao<V extends Serializable, K extends Serializable> {
    V select(K k);
    K insert(V v);
    void delete(K k);
    List<V> list();
    long count();
    void update(V v);
    void saveOrUpdate(V v);
}
