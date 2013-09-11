package com.odong.server.form.admin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:55
 */
public class RegProtocolForm implements Serializable {
    private static final long serialVersionUID = -8581738603651826461L;
    @NotNull
    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
