package com.odong.portal.form.admin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午12:40
 */
public class AboutMeForm implements Serializable {
    private static final long serialVersionUID = 1849637209053510610L;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
