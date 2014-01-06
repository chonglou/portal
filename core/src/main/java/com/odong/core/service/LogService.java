package com.odong.core.service;

import com.odong.core.entity.Log;

import java.util.List;

/**
 * Created by flamen on 13-12-30下午3:50.
 */
public interface LogService {
    List<Log> least(Long user, int size);

    List<Log> list(Long user, long start, int size);

    void add(Long user, String message, Log.Type type);

    void delete(long id);

    int count(Long user);
}
