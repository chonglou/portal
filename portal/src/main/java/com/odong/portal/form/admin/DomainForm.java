package com.odong.portal.form.admin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-17
 * Time: 下午5:04
 */
public class DomainForm implements Serializable {
    private static final long serialVersionUID = -2936766988806359075L;
    @NotNull
    private String domain;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}
