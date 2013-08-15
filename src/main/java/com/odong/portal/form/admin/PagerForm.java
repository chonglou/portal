package com.odong.portal.form.admin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-14
 * Time: 下午5:16
 */
public class PagerForm implements Serializable{
    private static final long serialVersionUID = 701886481651194175L;
    private int hotTagCount;
    private int hotArticleCount;
    private int latestCommentCount;
    private int archiveCount;
    private int articlePageSize;

    public int getHotTagCount() {
        return hotTagCount;
    }

    public void setHotTagCount(int hotTagCount) {
        this.hotTagCount = hotTagCount;
    }

    public int getHotArticleCount() {
        return hotArticleCount;
    }

    public void setHotArticleCount(int hotArticleCount) {
        this.hotArticleCount = hotArticleCount;
    }

    public int getLatestCommentCount() {
        return latestCommentCount;
    }

    public void setLatestCommentCount(int latestCommentCount) {
        this.latestCommentCount = latestCommentCount;
    }

    public int getArchiveCount() {
        return archiveCount;
    }

    public void setArchiveCount(int archiveCount) {
        this.archiveCount = archiveCount;
    }

    public int getArticlePageSize() {
        return articlePageSize;
    }

    public void setArticlePageSize(int articlePageSize) {
        this.articlePageSize = articlePageSize;
    }
}
