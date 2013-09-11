package com.odong.server.service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:34
 */
public interface RbacService {

    List<Long> listAdmin();

    void bindAdmin(long user, boolean bind);

    boolean authAdmin(long user);

    public enum OperationType {
        MANAGER
    }
}
