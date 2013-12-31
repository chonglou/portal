package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

import java.util.Date;

/**
 * Created by flamen on 13-12-31上午11:16.
 */
public class Tag extends IdEntity {
    private static final long serialVersionUID = 1100653236469231820L;
    private String name;
    private long visits;
    private int version;
    private Date lastEdit;
    private boolean keep;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }
}
