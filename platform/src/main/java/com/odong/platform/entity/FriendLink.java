package com.odong.platform.entity;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-30下午9:42.
 */
public class FriendLink extends IdEntity {
    private static final long serialVersionUID = 904088314641403216L;
    private String name;
    private String url;
    private String logo;
    private String details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
