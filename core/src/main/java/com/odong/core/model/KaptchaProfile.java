package com.odong.core.model;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-3上午10:01.
 */
public class KaptchaProfile implements Serializable {
    private static final long serialVersionUID = 8555988513637992582L;
    private int width;
    private int height;
    private int length;
    private String chars;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }
}
