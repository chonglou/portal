package com.odong.portal.form.cms;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午3:33
 */
public class CommentForm implements Serializable {
    private static final long serialVersionUID = 4818866119895600775L;
    @NotNull
    private String content;
    @NotNull
    private Long article;
    private Long comment;

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }
}
