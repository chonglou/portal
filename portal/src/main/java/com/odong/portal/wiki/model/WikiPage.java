package com.odong.portal.wiki.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 13-12-19下午8:27.
 */
public class WikiPage implements Serializable {
    public enum State {
        PUBLIC, PROTECTED, PRIVATE
    }

    private static final long serialVersionUID = -6009666721520210151L;
    private String name;
    private String title;
    private String body;
    private Date lastEdit;
    private Date created;
    private State state;
    private int version;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
