package com.odong.platform.form.admin;

import com.odong.core.entity.User;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:49.
 */
public class UserForm implements Serializable {
    private static final long serialVersionUID = 4060977919471500399L;
    private long id;
    private User.State state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User.State getState() {
        return state;
    }

    public void setState(User.State state) {
        this.state = state;
    }
}
