package com.odong.web.model;

import com.odong.core.entity.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 14-1-2下午8:42.
 */
public class SessionItem implements Serializable {
    public SessionItem(long ssUserId, String ssUsername, String ssLogo, String ssEmail) {
        this.ssUserId = ssUserId;
        this.ssUsername = ssUsername;
        this.ssLogo = ssLogo;
        this.ssEmail = ssEmail;
        this.ssCreated = new Date();
    }

    @Deprecated
    public SessionItem() {
    }

    private static final long serialVersionUID = 5020654086102364246L;
    private long ssUserId;
    private String ssUsername;
    private String ssLogo;
    private String ssEmail;
    private User.Type ssType;
    private boolean ssAdmin;
    private Date ssCreated;

    public User.Type getSsType() {
        return ssType;
    }

    public void setSsType(User.Type ssType) {
        this.ssType = ssType;
    }

    public long getSsUserId() {
        return ssUserId;
    }

    public void setSsUserId(long ssUserId) {
        this.ssUserId = ssUserId;
    }

    public String getSsUsername() {
        return ssUsername;
    }

    public void setSsUsername(String ssUsername) {
        this.ssUsername = ssUsername;
    }

    public String getSsLogo() {
        return ssLogo;
    }

    public void setSsLogo(String ssLogo) {
        this.ssLogo = ssLogo;
    }

    public String getSsEmail() {
        return ssEmail;
    }

    public void setSsEmail(String ssEmail) {
        this.ssEmail = ssEmail;
    }

    public boolean isSsAdmin() {
        return ssAdmin;
    }

    public void setSsAdmin(boolean ssAdmin) {
        this.ssAdmin = ssAdmin;
    }

    public Date getSsCreated() {
        return ssCreated;
    }

    public void setSsCreated(Date ssCreated) {
        this.ssCreated = ssCreated;
    }
}
