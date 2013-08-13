package com.odong.portal.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-21
 * Time: 下午1:28
 */
public class RegisterForm implements Serializable {
    private static final long serialVersionUID = 6494358806811756445L;
    @Email
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String newPwd;
    @NotNull
    private String rePwd;
    private boolean agree;

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getRePwd() {
        return rePwd;
    }

    public void setRePwd(String rePwd) {
        this.rePwd = rePwd;
    }
}
