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

    long countTag(Long article);


    List<Tag> hotTag(int count);

    List<Tag> listTag();

    List<Tag> listTagByArticle(Long article);

    Tag getTag(long id);

    Tag getTag(String name);

    void addTag(String name, boolean keep);

    void setTagName(long id, String name);

    void setTagVisits(long tag);

    void delTag(long id);

    void delTagByArticle(Long article);

    long countComment();

    long countCommentByUser(long user);

    long countCommentByArticle(Long article);

    Comment getComment(long comment);

    List<Comment> listComment();

    List<Comment> listComment(int no, int size);

    List<Comment> listCommentByArticle(Long article);

    List<Comment> listCommentByUser(long user);

    List<Comment> latestComment(int count);

    void editComment(long comment, String content);

    void addComment(Long user, Long article, Long comment, String content);

    void delComment(long id);

    long countArticle();

    long countArticleByAuthor(long author);

    long countArticleByTag(long tag);

    Article getArticle(Long article);

    ArticleTag getArticleTag(Long article, long tag);

    void delArticle(Long article);

    List<Article> listArticle();

    List<Article> latestArticle(int count);

    List<Article> hotArticle(int count);

    List<Article> listArticle(int no, int size);

    List<Article> listArticleByMonth(int year, int month);

    List<Article> listArticleByAuthor(long author);

    List<Article> listArticleByTag(long tag);

    List<ArticleTag> listArticleTag();

    Long addArticle(long author, String title, String summary, String body);

    void setArticleState(Long article, Article.State state);

    void setArticle(Long id, String title, String summary, String body);


    void bindArticleTag(Long article, long tag);

    void setArticleVisits(Long article);

    void setUserVisits(long user);

}
