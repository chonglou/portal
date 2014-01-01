package com.odong.core.model;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-31下午2:50.
 */
public class GoogleAuthProfile implements Serializable {
    private static final long serialVersionUID = -1229352721071877128L;
    private String id;
    private String uri;
    private String secret;
    private boolean enable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
