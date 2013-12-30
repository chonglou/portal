package com.odong.core.entity;

import java.util.Date;

/**
 * Created by flamen on 13-12-30上午2:22.
 */
public class User extends IdEntity {
    public enum Type{
        EMAIL,QQ,GOOGLE
    }
    public enum State{
        LOCKED, SUBMIT, ENABLE, DISABLE
    }
    private static final long serialVersionUID = 1231841634861834208L;
    private String uid;
    private String username;
    private String password;
    private String logo;
    private String nickname;
    private Date lastLogin;
    private String extra;
    private Type type;
    private State state;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
