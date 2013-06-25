package com.odong.portal.service;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Comment;
import com.odong.portal.entity.Tag;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
public interface ContentService {
    long countTag();

    long countTag(long article);

    List<Tag> topTag(int count);

    List<Tag> hotTag(int count);

    List<Tag> listTag();

    List<Tag> listTagByArticle(long article);

    Tag getTag(long id);

    Tag getTag(String name);

    void addTag(String name);

    void setTagName(long id, String name);

    void setTagVisit(long tag);

    void delTag(long id);

    long countComment();

    long countCommentByUser(long user);

    long countCommentByArticle(long article);

    Comment getComment(long comment);

    List<Comment> listComment(int no, int size);

    List<Comment> listCommentByArticle(long article);

    List<Comment> listCommentByUser(long user);

    List<Comment> latestComment(int count);

    void editComment(long comment, String content);

    void addComment(long user, long article, String content);

    void delComment(long id);

    long countArticle();

    long countArticleByAuthor(long author);

    long countArticleByTag(long tag);

    Article getArticle(long article);

    void delArticle(long article);

    List<Article> listArticle();

    List<Article> latestArticle(int count);

    List<Article> hotArticle(int count);

    List<Article> listArticle(int no, int size);

    List<Article> listArticleByMonth(int year, int month);

    List<Article> listArticleByAuthor(long author);

    List<Article> listArticleByTag(long tag);

    void addArticle(long author, String title, String summary, String body);

    void setArticleState(long article, Article.State state);

    void editArticle(long id, String title, String summary, String body);

    void setArticleTag(long article, long tag, boolean bind);

    void setArticleVisits(long article);

}
