package com.odong.platform.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by flamen on 14-1-6下午12:37.
 */
public class RegisterForm implements Serializable {
    private static final long serialVersionUID = 8206675058412312262L;
    @NotNull
    @Size(min = 2, max = 20)
    private String username;
    @Email
    @NotNull(message = "{valid.email}")
    private String email;
    @NotNull
    @Size(min = 6, max = 20)
    private String newPwd;
    @NotNull
    private String rePwd;
    private boolean agree;

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

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
