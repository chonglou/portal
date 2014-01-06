package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:43.
 */
public class GoogleAuthForm implements Serializable {
    private static final long serialVersionUID = -7663984302274444064L;
    private String secret;
    private String id;
    private String uri;
    private boolean enable;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
