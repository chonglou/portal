package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

import java.util.Date;

/**
 * Created by flamen on 13-12-31上午11:16.
 */
public class Comment extends IdEntity {
    private static final long serialVersionUID = 3153799395484463482L;
    private long user;
    private Long comment;
    private long article;
    private String content;
    private int version;
    private Date lastEdit;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }
}
