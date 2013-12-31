package com.odong.core.entity;

import java.util.Date;

/**
 * Created by flamen on 13-12-30下午7:32.
 */
public class Task extends IdEntity {

    private static final long serialVersionUID = -5799118644315913994L;

    private String request;
    private String module;
    private String type;
    private Date begin;
    private Date end;
    private Date nextRun;
    private long index;
    private long total;
    private int space;
    private int version;


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getNextRun() {
        return nextRun;
    }

    public void setNextRun(Date nextRun) {
        this.nextRun = nextRun;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
