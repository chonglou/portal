package com.odong.portal.entity.cms;

import com.odong.portal.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by flamen on 13-12-10 上午10:00.
 */
@Entity
@Table(name = "cmsResource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Statics extends IdEntity {
    public enum Type {
        BOOK, VIDEO
    }

    private static final long serialVersionUID = 8205725590166953432L;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(nullable = false)
    private String details;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false, updatable = false)
    private Date created;
    @Version
    private int version;
    @Column(nullable = false)
    private long visits;
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }
}
