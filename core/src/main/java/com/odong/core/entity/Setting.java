package com.odong.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 13-12-30上午2:34.
 */
public class Setting implements Serializable {
    private static final long serialVersionUID = -4386800822459556058L;
    private String key;
    private String value;
    private int version;
    private Date created;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
