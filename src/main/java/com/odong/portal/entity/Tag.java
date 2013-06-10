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
 * Time: 下午2:30
 */
@Entity
@Table(name = "cmsTag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag extends IdEntity {
    public Date getPublishDate() {
        return lastEdit == null ? created : lastEdit;
    }

    private static final long serialVersionUID = -2365008447006155462L;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Long visits;
    @Version
    private int version;
    private Date created;
    @Column(nullable = false, updatable = false)
    private Date lastEdit;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
