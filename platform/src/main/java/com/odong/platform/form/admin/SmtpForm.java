package com.odong.platform.form.admin;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:34.
 */
public class SmtpForm implements Serializable {
    private static final long serialVersionUID = -7553866026073993197L;
     @Email(message = "{val.email}")
    private String bcc;
    @NotNull
    private String host;
    private boolean ssl;

    private int port;
    @NotNull
    @Size(min = 2, max = 20, message = "{val.name}")
    private String username;
    @NotNull
    @Size(min = 6, max = 50, message = "{val.password}")
    private String password;

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
