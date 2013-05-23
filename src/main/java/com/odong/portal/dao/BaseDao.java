package com.odong.portal.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:30
 */
public interface BaseDao<T extends Serializable, PK extends Serializable> {
    T select(PK id);
    void insert(T t);
    void delete(PK id);
    List<T> list();
    long count();
    void update(T t);
}
