package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 14-1-4上午12:22.
 */
public class Statics extends IdEntity {
    public enum Type{
        VIDEO,BOOK
    }
    private static final long serialVersionUID = -138917445473197921L;
    private String name;
    private String details;
    private String url;
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
