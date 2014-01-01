package com.odong.web;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-3
 * Time: 上午11:22
 */
public class Link implements Serializable {
    public Link(String name, String url) {
        this.url = url;
        this.name = name;
    }

    @Deprecated
    public Link() {
    }

    private static final long serialVersionUID = -7821227957600602570L;
    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
