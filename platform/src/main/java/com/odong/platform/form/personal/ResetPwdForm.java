package com.odong.platform.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by flamen on 14-1-6上午11:36.
 */
public class ResetPwdForm implements Serializable {
    private static final long serialVersionUID = 9063477463560476157L;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min = 6, max = 20)
    private String newPwd;
    @NotNull
    private String rePwd;
    @NotNull
    private String captcha;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
