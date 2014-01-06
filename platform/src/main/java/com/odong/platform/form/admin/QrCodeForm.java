package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:30.
 */
public class QrCodeForm implements Serializable {
    private static final long serialVersionUID = 7792026939235061979L;
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
