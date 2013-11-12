package com.odong.portal.model.profile;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午2:11
 */
public class QrCodeProfile implements Serializable {
    @Deprecated
    public QrCodeProfile() {
    }

    public QrCodeProfile(String content, int width, int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    private static final long serialVersionUID = -3197437194548590193L;
    private String content;
    private int width;
    private int height;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
