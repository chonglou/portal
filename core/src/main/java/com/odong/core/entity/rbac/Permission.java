package com.odong.core.entity.rbac;

import com.odong.core.entity.IdEntity;

import java.util.Date;

/**
 * Created by flamen on 13-12-31下午6:11.
 */
public class Permission extends IdEntity {
    private static final long serialVersionUID = 7149266874160443957L;
    private long role;
    private long resource;
    private long operation;
    private Date startup;
    private Date shutDown;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    public long getResource() {
        return resource;
    }

    public void setResource(long resource) {
        this.resource = resource;
    }

    public long getOperation() {
        return operation;
    }

    public void setOperation(long operation) {
        this.operation = operation;
    }

    public Date getStartup() {
        return startup;
    }

    public void setStartup(Date startup) {
        this.startup = startup;
    }

    public Date getShutDown() {
        return shutDown;
    }

    public void setShutDown(Date shutDown) {
        this.shutDown = shutDown;
    }
}
