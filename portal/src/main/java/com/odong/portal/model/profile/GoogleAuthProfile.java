package com.odong.portal.model.profile;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-13
 * Time: 下午2:27
 */
public class GoogleAuthProfile implements Serializable {
    private static final long serialVersionUID = -8356614490772478744L;
    private String id;
    private String uri;
    private String secret;
    private boolean enable;

    public GoogleAuthProfile(String id, String uri, String secret) {
        this.id = id;
        this.uri = uri;
        this.secret = secret;
    }

    @Deprecated
    public GoogleAuthProfile() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
