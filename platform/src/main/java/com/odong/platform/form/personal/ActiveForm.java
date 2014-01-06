package com.odong.platform.form.personal;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by flamen on 14-1-6下午12:40.
 */
public class ActiveForm implements Serializable {
    private static final long serialVersionUID = -3936906107690042413L;
    private boolean agree;
    @Email(message = "valid.email")
    @NotNull
    private String email;

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
