package com.odong.server.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-21
 * Time: 下午1:28
 */
public class RegisterForm implements Serializable {
    private static final long serialVersionUID = 6494358806811756445L;

    @NotNull
    @Size(min = 2, max = 20, message = "{valid.name}")
    private String username;
    @Email
    @NotNull(message = "{valid.email}")
    private String email;
    @NotNull
    @Size(min = 6, max = 20, message = "{valid.password}")
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
