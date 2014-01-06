package com.odong.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 14-1-5下午8:12.
 */
public class RssItem implements Serializable {
    private static final long serialVersionUID = 4585401422151420225L;
    private String url;
    public String title;
    public String summary;
    private Date publish;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getPublish() {
        return publish;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }
}
