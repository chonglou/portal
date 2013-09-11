package com.odong.server.form.admin;

import com.odong.server.entity.User;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:28
 */
public class UserForm implements Serializable {
    private static final long serialVersionUID = 6335838906516973056L;
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
