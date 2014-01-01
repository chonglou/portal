package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-31下午7:47.
 */
public class FriendLink extends IdEntity {
    private static final long serialVersionUID = -6334818554812210989L;
    private String name;
    private String url;
    private String logo;

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

}
