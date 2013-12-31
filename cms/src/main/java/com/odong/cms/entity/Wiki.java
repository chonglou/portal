package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

import java.util.Date;

/**
 * Created by flamen on 13-12-31上午11:16.
 */
public class Wiki extends IdEntity {
    private static final long serialVersionUID = 4011807820019616806L;
    private String name;
    private String title;
    private String body;
    private Date lastEdit;
    private int version;
    private long user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }
}
