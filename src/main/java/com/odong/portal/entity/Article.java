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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {
    public enum State {
        PRIVATE, PUBLIC, PROTECTED
    }

    public Date getPublishDate(){
        return lastEdit == null ? created : lastEdit;
    }

    private static final long serialVersionUID = 9130835572844462147L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 500)
    private String summary;
    @Lob
    @Column(nullable = false)
    private String body;
    @Column(nullable = false, updatable = false)
    private Long author;
    @Column(nullable = false, updatable = false)
    private Date created;
    private Date lastEdit;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(nullable = false)
    private Long visits;
    @Version
    private int version;

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

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
}
