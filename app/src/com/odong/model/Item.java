package com.odong.model;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-10 下午1:48.
 */
public class Item implements Serializable {
    public final static String NAME = "name";
    public final static String DETAILS = "details";
    private static final long serialVersionUID = -5757196379322842461L;
    private String name;
    private String url;
    private String details;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
