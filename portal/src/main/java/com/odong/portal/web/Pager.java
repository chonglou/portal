package com.odong.portal.web;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-5
 * Time: 下午10:09
 */
public class Pager implements Serializable {
    public Pager(String url, int size, int index, int total) {
        this.url = url;
        this.size = size;
        this.index = index;
        this.total = total;
    }

    @Deprecated
    public Pager() {
    }

    private static final long serialVersionUID = 1457926013346750008L;
    private String url;
    private int size;
    private int index;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
