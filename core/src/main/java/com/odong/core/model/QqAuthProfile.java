package com.odong.core.model;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-31下午2:50.
 */
public class QqAuthProfile implements Serializable {
    public QqAuthProfile(String valid, String id, String key, String uri) {
        this.valid = valid;
        this.id = id;
        this.key = key;
        this.uri = uri;
    }

    @Deprecated
    public QqAuthProfile() {
    }

    private static final long serialVersionUID = 1364265699069501727L;
    private String valid;
    private String id;
    private String key;
    private String uri;
    private boolean enable;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
