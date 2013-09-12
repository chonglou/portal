package com.odong.portal.entity;


import com.odong.portal.web.Page;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
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
    public Page toPage() {
        return new Page(name, "/tag/" + getId());
    }

    public Date getPublishDate() {
        return lastEdit == null ? created : lastEdit;
    }

    private static final long serialVersionUID = -2365008447006155462L;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private long visits;
    @Version
    private int version;
    @Column(nullable = false, updatable = false)
    private Date created;
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

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
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
