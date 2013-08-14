package com.odong.portal.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午5:38
 */

@Entity
@Table(name = "friendLink")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FriendLink extends IdEntity {
    private static final long serialVersionUID = 5148015255031294863L;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
