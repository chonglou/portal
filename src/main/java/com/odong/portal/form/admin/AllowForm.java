package com.odong.portal.form.admin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-1
 * Time: 上午2:32
 */
public class AllowForm implements Serializable {
    private static final long serialVersionUID = -7689227883805567018L;
    private boolean allowRegister;
    private boolean allowLogin;

    public boolean isAllowRegister() {
        return allowRegister;
    }

    public void setAllowRegister(boolean allowRegister) {
        this.allowRegister = allowRegister;
    }

    public boolean isAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(boolean allowLogin) {
        this.allowLogin = allowLogin;
    }
}
