package com.odong.platform.form.personal;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-30下午9:42.
 */
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 6482637948116279342L;
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
