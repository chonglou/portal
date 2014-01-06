package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:50.
 */
public class ManagerForm implements Serializable {
    private static final long serialVersionUID = 526727301128761810L;
    private long userId;
    private boolean bind;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }
}
