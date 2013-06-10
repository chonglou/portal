package com.odong.portal.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:43
 */
@Entity
@Table(name = "ucLog")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Log extends IdEntity {
    public enum Type {
        INFO, DEBUG, ERROR
    }

    private static final long serialVersionUID = -3691580318696379338L;
    @Lob
    @Column(nullable = false, updatable = false)
    private String message;
    @Column(updatable = false)
    private Long user;
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false, updatable = false)
    private Date created;


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
