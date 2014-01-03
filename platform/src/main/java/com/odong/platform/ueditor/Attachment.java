package com.odong.platform.ueditor;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-13
 * Time: 上午10:10
 */
public class Attachment implements Serializable {
    public enum State {
        SUCCESS("成功"),
        NOFILE("未包含文件上传域"),
        TYPE("不允许的文件格式"),
        SIZE("文件大小超出限制"),
        ENTYPE("请求类型ENTYPE错误"),
        HEADER("请求地址头不正确"),
        URL("请求地址不存在！"),
        REQUEST("上传请求异常"),
        IO("IO异常"),
        DIR("目录创建失败"),
        UNKNOWN("未知错误");


        @Override
        public String toString() {
            return value;
        }

        private State(String value) {
            this.value = value;
        }

        private final String value;
    }

    // 输出文件地址
    private String url;
    // 上传文件名
    private String fileName;
    // 状态
    private State state;
    // 文件类型
    private String type;
    // 原始文件名
    private String originalName;
    private String title;
    private static final long serialVersionUID = 3579737443120928059L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
