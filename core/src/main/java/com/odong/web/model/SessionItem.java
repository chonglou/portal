package com.odong.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 14-1-2下午8:42.
 */
public class SessionItem implements Serializable {
    public SessionItem(long ssId, String ssUsername, String ssLogo, String ssEmail) {
        this.ssId = ssId;
        this.ssUsername = ssUsername;
        this.ssLogo = ssLogo;
        this.ssEmail = ssEmail;
        this.ssCreated = new Date();
    }

    @Deprecated
    public SessionItem() {
    }

    private static final long serialVersionUID = 5020654086102364246L;
    public final static String KEY="j8l0i4h7w3";
    private long ssId;
    private String ssUsername;
    private String ssLogo;
    private String ssEmail;
    private String ssType;
    private boolean ssAdmin;
    private Date ssCreated;

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }

    public long getSsId() {
        return ssId;
    }

    public void setSsId(long ssId) {
        this.ssId = ssId;
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
