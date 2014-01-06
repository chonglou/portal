package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:39.
 */
public class AllowForm implements Serializable {
    private static final long serialVersionUID = -9111887003999756817L;
    private boolean allowRegister;
    private boolean allowLogin;
    private boolean allowAnonym;

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

    public boolean isAllowAnonym() {
        return allowAnonym;
    }

    public void setAllowAnonym(boolean allowAnonym) {
        this.allowAnonym = allowAnonym;
    }
}
