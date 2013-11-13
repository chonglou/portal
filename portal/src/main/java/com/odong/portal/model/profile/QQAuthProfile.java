package com.odong.portal.model.profile;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午4:22
 */
public class QQAuthProfile implements Serializable {
    public QQAuthProfile(String valid, String id, String key) {
        this.valid = valid;
        this.id = id;
        this.key = key;
    }

    @Deprecated
    public QQAuthProfile() {
    }

    private static final long serialVersionUID = -2603305603672092832L;
    private String valid;
    private String id;
    private String key;
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

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
}
