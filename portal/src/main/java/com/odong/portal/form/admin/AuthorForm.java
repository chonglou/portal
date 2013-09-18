package com.odong.portal.form.admin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-18
 * Time: 上午12:47
 */
public class AuthorForm implements Serializable {
    private static final long serialVersionUID = -5279563152959442265L;
    private long author;
    private long article;

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }
}
