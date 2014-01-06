package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午8:58.
 */
public class AboutMeForm implements Serializable {
    private static final long serialVersionUID = -1044974998835475955L;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
