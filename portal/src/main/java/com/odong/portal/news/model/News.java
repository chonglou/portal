package com.odong.portal.news.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 13-12-20上午1:44.
 */
public class News implements Serializable {
    private static final long serialVersionUID = 8234564428796968547L;
    private String title;
    private String body;
    private String url;
    private String source;
    private Date created;
    private Date lastEdit;
    private int version;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
