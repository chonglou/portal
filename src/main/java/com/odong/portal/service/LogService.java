package com.odong.portal.service;

import com.odong.portal.entity.Log;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:42
 */
public interface LogService {
    void log(String message, Log.Type type);

    void log(long user, String message, Log.Type type);
}
