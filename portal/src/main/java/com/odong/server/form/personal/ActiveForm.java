package com.odong.server.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:44
 */
public class ActiveForm implements Serializable {
    private static final long serialVersionUID = -8643401225025746727L;
    private boolean agree;
    @Email(message = "valid.email")
    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
