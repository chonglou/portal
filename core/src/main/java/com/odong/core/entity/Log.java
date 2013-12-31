package com.odong.core.entity;

/**
 * Created by flamen on 13-12-30上午2:20.
 */
public class Log extends IdEntity {
    public enum Type {
        DEBUG, ERROR, INFO
    }

    private static final long serialVersionUID = -1830152528089278813L;
    private Long user;
    private String message;
    private Type type;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
