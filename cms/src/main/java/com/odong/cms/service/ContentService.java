package com.odong.cms.service;

import com.odong.cms.entity.Article;
import com.odong.cms.entity.ArticleTag;
import com.odong.cms.entity.Comment;
import com.odong.cms.entity.Tag;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午6:08.
 */
public interface ContentService {


    int countTag();

    int countTag(long article);


    List<Tag> hotTag(int count);

    List<Tag> listTag();

    List<Tag> listTagByArticle(long article);

    Tag getTag(long id);

    Tag getTag(String name);

    Long addTag(String name, boolean keep);

    void setTagName(long id, String name);

    void setTagVisits(long tag);

    void delTag(long id);

    int countComment();

    int countCommentByUser(long user);

    int countCommentByArticle(long article);

    Comment getComment(long comment);

    List<Comment> listComment();

    List<Comment> listComment(long start, int size);

    List<Comment> listCommentByArticle(long article);

    List<Comment> listCommentByUser(long user);

    List<Comment> latestComment(int count);

    void editComment(long comment, String content);

    void addComment(Long user, long article, Long comment, String content);

    void delComment(long id);

    int countArticle();

    int countArticleByAuthor(long author);

    int countArticleByTag(long tag);

    Article getArticle(long article);

    void delArticle(long article);

    List<Article> listArticle();

    List<Article> latestArticle(int count);

    List<Article> hotArticle(int count);

    List<Article> listArticle(long start, int size);

    List<Article> listArticleByAuthor(long author);

    List<Article> listArticleByTag(long tag);

    List<Long> getTagIdsByArticle(long article);

    List<Long> getArticleIdsByTag(long tag);

    List<Long> getArticleIdsByPage(long start, int size);

    List<Long> getArticleIdsByAuthor(long author);

    List<Long> getArticleIdsByCreated(Date begin, Date end);

    List<Long> getArticleIdsBySearch(String key);

    long addArticle(long author, String logo, String title, String summary, String body);

    void setArticleAuthor(long article, long user);

    void setArticle(long id, String logo, String title, String summary, String body);

    void bindArticleTag(long article, long tag, boolean bind);

    void setArticleVisits(long article);

    void setUserVisits(long user);
}
