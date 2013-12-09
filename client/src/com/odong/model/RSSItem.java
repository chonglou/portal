package com.odong.model;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-9.
 */
public class RSSItem implements Serializable {
    private static final long serialVersionUID = 1687779784478973789L;
    public final static String TITLE="title";
    public final static String PUB_DATE = "pubDate";
    private String title;
    private String pubDate;
    private String description;
    private String link;
    private String category;

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
