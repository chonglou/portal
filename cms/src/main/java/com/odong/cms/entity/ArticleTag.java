package com.odong.cms.entity;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-31上午11:16.
 */
public class ArticleTag extends IdEntity {
    private static final long serialVersionUID = 6333808146048330825L;
    private long article;
    private long tag;

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    public long getTag() {
        return tag;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }
}
