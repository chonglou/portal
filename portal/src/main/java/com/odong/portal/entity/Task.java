package com.odong.portal.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午7:54
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {
    public enum Type {
        EMAIL, RSS, SITE_MAP, GC, BACKUP, IMPORT, EXPORT,VISIT
    }

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;
    @Lob
    private String request;
    @Column(nullable = false, updatable = false)
    private Date crated;
    @Column(nullable = false, updatable = false)
    private Date begin;
    @Column(nullable = false, updatable = false)
    private Date end;
    @Column(nullable = false)
    private Date nextRun;
    @Column(nullable = false)
    private long index;
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Version
    private int version;
    private static final long serialVersionUID = 5719367084733563338L;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCrated() {
        return crated;
    }

    public void setCrated(Date crated) {
        this.crated = crated;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getNextRun() {
        return nextRun;
    }

    public void setNextRun(Date nextRun) {
        this.nextRun = nextRun;
    }
}
