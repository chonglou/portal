package com.odong.portal.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午11:52
 */

@Entity
@Table(name = "ucOpenId")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OpenId extends IdEntity {
    public enum Type {
        QQ, GOOGLE
    }

    private static final long serialVersionUID = 8161901734311168752L;
    @Column(nullable = false, updatable = false, unique = true)
    private String oid;
    @Column(length = 512, nullable = false)
    private String token;
    @Column(nullable = false, updatable = false)
    private Long user;
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Lob
    private String profile;
    @Column
    private Date lastEdit;
    @Column(nullable = false, updatable = false)
    private Date created;

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
