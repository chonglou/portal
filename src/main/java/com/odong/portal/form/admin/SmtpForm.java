package com.odong.portal.form.admin;

import com.odong.portal.validator.Port;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午1:21
 */
public class SmtpForm implements Serializable {
    private static final long serialVersionUID = 1768811481444161037L;
    @Email(message = "{val.email}")
    private String bcc;
    @NotNull
    private String host;
    private boolean ssl;
    @Port
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
