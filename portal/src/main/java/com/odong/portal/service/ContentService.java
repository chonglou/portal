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
    List<Article> search(String key);

    long countTag();

    long countTag(long article);


    List<Tag> hotTag(int count);

    List<Tag> listTag();

    List<Tag> listTagByArticle(long article);

    Tag getTag(long id);

    Tag getTag(String name);

    Long addTag(String name, boolean keep);

    void setTagName(long id, String name);

    void setTagVisits(long tag);

    void delTag(long id);

    void delTagByArticle(long article);

    long countComment();

    long countCommentByUser(long user);

    long countCommentByArticle(long article);

    Comment getComment(long comment);

    List<Comment> listComment();

    List<Comment> listComment(int no, int size);

    List<Comment> listCommentByArticle(long article);

    List<Comment> listCommentByUser(long user);

    List<Comment> latestComment(int count);

    void editComment(long comment, String content);

    void addComment(Long user, long article, Long comment, String content);

    void delComment(long id);

    long countArticle();

    long countArticleByAuthor(long author);

    long countArticleByTag(long tag);

    Article getArticle(long article);

    ArticleTag getArticleTag(long article, long tag);

    void delArticle(long article);

    List<Article> listArticle();

    List<Article> latestArticle(int count);

    List<Article> hotArticle(int count);

    List<Article> listArticle(int no, int size);

    List<Article> listArticleByMonth(int year, int month);

    List<Article> listArticleByAuthor(long author);

    List<Article> listArticleByTag(long tag);

    List<ArticleTag> listArticleTag();

    Long addArticle(long author, String logo, String title, String summary, String body);

    void setArticleAuthor(long article, long user);
    void setArticleState(long article, Article.State state);

    void setArticle(long id, String logo, String title, String summary, String body);


    void bindArticleTag(long article, long tag);

    void setArticleVisits(long article);

    void setUserVisits(long user);

}
