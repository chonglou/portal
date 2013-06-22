package com.odong.portal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:03
 */
public class SessionItem implements Serializable {
    private static final long serialVersionUID = -1606662743090417130L;
    private Long userId;
    private String username;
    private String email;
    private Date created;
    private boolean isAdmin;
    public final static String KEY = "d1s7e0wp";



    public SessionItem(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.created = new Date();
    }

    public SessionItem() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
