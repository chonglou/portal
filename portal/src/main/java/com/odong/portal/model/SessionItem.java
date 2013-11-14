package com.odong.portal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:03
 */
public class SessionItem implements Serializable {
    private static final long serialVersionUID = -1606662743090417130L;
    private Long ssUserId;
    private String ssUsername;
    private String ssEmail;
    private Date ssCreated;
    private boolean ssAdmin;
    private String ssType;
    private String ssLogo;
    public final static String KEY = "d1s7e0wp";

    public SessionItem(long userId, String email, String username) {
        this.ssUserId = userId;
        this.ssEmail = email;
        this.ssUsername = username;
        this.ssCreated = new Date();
        this.ssType = "local";
    }


    public boolean isSsLocal(){
        return "local".equals(ssType);
    }

    @Deprecated
    public SessionItem() {
    }

    public String getSsLogo() {
        return ssLogo;
    }

    public void setSsLogo(String ssLogo) {
        this.ssLogo = ssLogo;
    }

    public String getSsType() {
        return ssType;
    }

    public void setSsType(String ssType) {
        this.ssType = ssType;
    }


    public Long getSsUserId() {
        return ssUserId;
    }

    public void setSsUserId(Long ssUserId) {
        this.ssUserId = ssUserId;
    }

    public String getSsUsername() {
        return ssUsername;
    }

    public void setSsUsername(String ssUsername) {
        this.ssUsername = ssUsername;
    }

    public String getSsEmail() {
        return ssEmail;
    }

    public void setSsEmail(String ssEmail) {
        this.ssEmail = ssEmail;
    }

    public Date getSsCreated() {
        return ssCreated;
    }

    public void setSsCreated(Date ssCreated) {
        this.ssCreated = ssCreated;
    }

    public boolean isSsAdmin() {
        return ssAdmin;
    }

    public void setSsAdmin(boolean ssAdmin) {
        this.ssAdmin = ssAdmin;
    }
}
