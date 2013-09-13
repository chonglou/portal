package com.odong.portal.entity;

import com.odong.portal.web.Page;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午3:13
 */
@Entity
@Table(name = "cmsComment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment extends IdEntity {
    public Page toPage() {
        return new Page(content, "/article/" + article + "#" + getId());
    }

    public Date getPublishDate() {
        return lastEdit == null ? created : lastEdit;
    }

    private static final long serialVersionUID = -9089422699866112475L;

    @Column(updatable = false)
    private Long user;
    @Column(updatable = false)
    private Long comment;
    @Column(nullable = false, updatable = false)
    private Long article;
    @Lob
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, updatable = false)
    private Date created;
    private Date lastEdit;
    @Version
    private int version;

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
