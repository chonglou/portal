package com.odong.core.service;

import java.util.Date;

/**
 * Created by flamen on 13-12-31下午6:13.
 */
public interface RbacService {
    void bind(String role, String operation, String resource, Date startup, Date shutdown, boolean bind);

    boolean auth(String role, String operation, String resource);
}
