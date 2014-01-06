package com.odong.core.model;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午3:24.
 */
public class QrCodeProfile implements Serializable {
    public QrCodeProfile(String content, int width, int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    @Deprecated
    public QrCodeProfile() {
    }

    private static final long serialVersionUID = 6090555538839693566L;
    private String content;
    private int width;
    private int height;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
