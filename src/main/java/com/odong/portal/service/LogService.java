package com.odong.portal.service;

import com.odong.portal.entity.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:42
 */
public interface LogService {
    void add(Long user, String message, Log.Type type);

    List<Log> list(int no, int size);

    void delete(Long id);
}
