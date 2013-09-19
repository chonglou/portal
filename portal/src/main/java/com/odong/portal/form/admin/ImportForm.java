package com.odong.portal.form.admin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-19
 * Time: 下午12:20
 */
public class ImportForm implements Serializable {
    private static final long serialVersionUID = 4392450282164100533L;
    @NotNull
    private String url;
    @NotNull
    private String captcha;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
