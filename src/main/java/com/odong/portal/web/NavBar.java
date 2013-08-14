package com.odong.portal.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-3
 * Time: 上午11:21
 */
public class NavBar implements Serializable {
    public enum  Type{
        LIST,MENU
    }
    public void add(String name, String url) {
        Page p = new Page();
        if (name.length() > 100) {
            name = name.substring(0, 97);
        }
        p.setName(name);
        p.setUrl(url);
        this.pages.add(p);
    }

    public NavBar(String title) {
        this.title = title;
        this.pages = new ArrayList<>();
        this.type = Type.MENU;
    }

    private static final long serialVersionUID = 9005274236641313051L;
    private Type type;
    private String title;
    private boolean ajax;
    private List<Page> pages;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
