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
    private boolean ssLocal;
    public final static String KEY = "d1s7e0wp";
    public SessionItem(long userId, String email, String username){
        this.ssUserId = userId;
        this.ssEmail = email;
        this.ssUsername = username;
        this.ssCreated = new Date();
        this.setSsLocal(true);
    }

    @Deprecated
    public SessionItem() {
    }

    public boolean isSsLocal() {
        return ssLocal;
    }

    public void setSsLocal(boolean ssLocal) {
        this.ssLocal = ssLocal;
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
