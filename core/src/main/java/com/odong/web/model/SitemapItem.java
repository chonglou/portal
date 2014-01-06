package com.odong.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 14-1-5下午8:13.
 */
public class SitemapItem implements Serializable {
    private static final long serialVersionUID = -2203641328975720533L;
    private String url;
    private Date publish;
    private double priority;
    private String changeFreq;

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

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public String getChangeFreq() {
        return changeFreq;
    }

    public void setChangeFreq(String changeFreq) {
        this.changeFreq = changeFreq;
    }
}
