package com.odong.core.rss;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 13-12-31下午10:31.
 */
public class RssContent implements Serializable {
    public RssContent() {
    }

    public RssContent(String title, String summary, String url, String author, Date publish) {
        this.summary = summary;
        this.title = title;
        this.url = url;
        this.author = author;
        this.publish = publish;
    }

    private static final long serialVersionUID = -2707683156463205314L;
    private String summary;
    private String title;
    private String url;
    private String author;
    private Date publish;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

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

    public Date getPublish() {
        return publish;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }
}
