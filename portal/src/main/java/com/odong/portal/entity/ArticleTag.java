package com.odong.portal.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:30
 */
@Entity
@Table(name = "cmsArticleTag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleTag extends IdEntity {
    private static final long serialVersionUID = 4989475576131553738L;

    @Column(nullable = false, updatable = false)
    private Long article;
    @Column(nullable = false, updatable = false)
    private Long tag;

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }
}
