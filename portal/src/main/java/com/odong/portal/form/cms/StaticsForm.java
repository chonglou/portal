package com.odong.portal.form.cms;

import com.odong.portal.entity.cms.Statics;

import java.io.Serializable;

/**
 * Created by flamen on 13-12-10 上午10:49.
 */
public class StaticsForm implements Serializable {
    private static final long serialVersionUID = -1807290691779863008L;
    private Long id;
    private String name;
    private String details;
    private Statics.Type type;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Statics.Type getType() {
        return type;
    }

    public void setType(Statics.Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
