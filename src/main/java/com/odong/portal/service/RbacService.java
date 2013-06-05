package com.odong.portal.service;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:34
 */
public interface RbacService {
    void setAdmin(long user);
    boolean isAdmin(long user);
    public enum OperationType{
        MANAGER
    }
}
