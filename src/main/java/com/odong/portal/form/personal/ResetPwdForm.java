package com.odong.portal.form.personal;

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
public class ResetPwdForm implements Serializable {
    private static final long serialVersionUID = -8930794573995829457L;
    @Email(message = "{valid.message}")
    @NotNull
    private String email;
    @NotNull
    @Size(min = 6, max = 20, message = "{valid.password}")
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
