package com.odong.portal.service;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.ArticleTag;
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

    long countTag(String article);


    List<Tag> hotTag(int count);

    List<Tag> listTag();

    List<Tag> listTagByArticle(String article);

    Tag getTag(long id);

    Tag getTag(String name);

    void addTag(String name);

    void setTagName(long id, String name);

    void setTagVisits(long tag);

    void delTag(long id);

    void delTagByArticle(String article);

    long countComment();

    long countCommentByUser(long user);

    long countCommentByArticle(String article);

    Comment getComment(long comment);

    List<Comment> listComment();

    List<Comment> listComment(int no, int size);

    List<Comment> listCommentByArticle(String article);

    List<Comment> listCommentByUser(long user);

    List<Comment> latestComment(int count);

    void editComment(long comment, String content);

    void addComment(Long user, String article, Long comment, String content);

    void delComment(long id);

    long countArticle();

    long countArticleByAuthor(long author);

    long countArticleByTag(long tag);

    Article getArticle(String article);

    ArticleTag getArticleTag(String article, long tag);

    void delArticle(String article);

    List<Article> listArticle();

    List<Article> latestArticle(int count);

    List<Article> hotArticle(int count);

    List<Article> listArticle(int no, int size);

    List<Article> listArticleByMonth(int year, int month);

    List<Article> listArticleByAuthor(long author);

    List<Article> listArticleByTag(long tag);

    List<ArticleTag> listArticleTag();

    void addArticle(String id, long author, String title, String summary, String body);

    void setArticleState(String article, Article.State state);

    void setArticle(String id, String title, String summary, String body);


    void bindArticleTag(String article, long tag);

    void setArticleVisits(String article);

}
