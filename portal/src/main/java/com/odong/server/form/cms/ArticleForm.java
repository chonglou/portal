package com.odong.server.form.cms;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午4:32
 */
public class ArticleForm implements Serializable {
    private static final long serialVersionUID = 7362366170765276351L;
    @NotNull
    private String title;
    @NotNull
    private String body;
    @NotNull
    private String tags;
    private String summary;
    private String id;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
