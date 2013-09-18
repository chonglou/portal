package com.odong.portal.service.impl;

import com.odong.portal.dao.*;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.ArticleTag;
import com.odong.portal.entity.Comment;
import com.odong.portal.entity.Tag;
import com.odong.portal.service.ContentService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:29
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {
    @Override
    public List<Article> search(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "%" + key + "%");
        return articleDao.list("FROM Article a WHERE a.title LIKE :key OR a.summary LIKE :key", map);  //
    }

    @Override
    public long countTag() {
        return tagDao.count();  //
    }

    @Override
    public long countTag(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        return tagDao.count("FROM ArticleTag  i WHERE i.article=:article", map);  //
    }


    @Override
    public List<Tag> hotTag(int count) {
        return tagDao.list("FROM Tag  i ORDER BY i.visits DESC", null, count);  //
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.list();  //
    }

    @Override
    public List<Tag> listTagByArticle(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);

        List<Tag> tags = new ArrayList<>();
        for (ArticleTag at : articleTagDao.list("FROM ArticleTag  i WHERE i.article=:article", map)) {
            tags.add(tagDao.select(at.getTag()));
        }
        return tags;  //
    }

    @Override
    public Tag getTag(long id) {
        return tagDao.select(id);  //
    }

    @Override
    public Tag getTag(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return tagDao.select("FROM Tag  i WHERE i.name=:name", map);  //
    }

    @Override
    public Long addTag(String name, boolean keep) {
        Tag t = new Tag();
        t.setName(name);
        t.setKeep(keep);
        t.setCreated(new Date());
        return tagDao.persist(t);
    }

    @Override
    public void setTagName(long id, String name) {
        Tag t = tagDao.select(id);
        t.setName(name);
        t.setLastEdit(new Date());
        tagDao.update(t);
    }

    @Override
    public void setTagVisits(long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", tag);
        tagDao.update("UPDATE Tag  i SET i.visits=i.visits+1 WHERE i.id=:id", map);
    }

    @Override
    public void delTag(long id) {
        tagDao.delete(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        articleTagDao.delete("DELETE Tag  i WHERE i.id=:id ", map);
    }

    @Override
    public void delTagByArticle(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        articleTagDao.delete("DELETE ArticleTag i WHERE i.article=:article", map);
    }


    @Override
    public long countComment() {
        return commentDao.count();  //
    }

    @Override
    public long countCommentByUser(long user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return commentDao.count("SELECT COUNT(*) FROM Comment  i WHERE i.user=:user", map);  //
    }

    @Override
    public long countCommentByArticle(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        return commentDao.count("SELECT COUNT(*) FROM Comment  i WHERE i.article=:article", map);  //
    }

    @Override
    public Comment getComment(long comment) {
        return commentDao.select(comment);
    }

    @Override
    public List<Comment> listComment() {
        return commentDao.list();
    }

    @Override
    public List<Comment> listComment(int no, int size) {
        return commentDao.list(no, size, commentDao.hqlListAll(), null);  //
    }

    @Override
    public List<Comment> listCommentByArticle(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        return commentDao.list("FROM Comment  i WHERE i.article=:article ORDER BY i.id DESC", map);  //

    }

    @Override
    public List<Comment> listCommentByUser(long user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return commentDao.list("FROM Comment  i WHERE i.user=:user ORDER BY i.id DESC", map);  //

    }

    @Override
    public List<Comment> latestComment(int count) {

        return commentDao.list(commentDao.hqlListAll(), null, count);  //
    }

    @Override
    public void editComment(long comment, String content) {
        Comment c = commentDao.select(comment);
        c.setContent(content);
        c.setLastEdit(new Date());
        commentDao.update(c);
    }

    @Override
    public void addComment(Long user, long article, Long comment, String content) {
        Comment c = new Comment();
        c.setUser(user);
        c.setArticle(article);
        c.setContent(content);
        c.setComment(comment);
        c.setCreated(new Date());
        commentDao.insert(c);
    }

    @Override
    public void delComment(long id) {
        commentDao.delete(id);
    }

    @Override
    public long countArticle() {
        return articleDao.count();  //
    }

    @Override
    public long countArticleByAuthor(long author) {
        Map<String, Object> map = new HashMap<>();
        map.put("author", author);
        return articleDao.count("SELECT COUNT(*) FROM Article  i WHERE i.author=:author", map);  //
    }

    @Override
    public long countArticleByTag(long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("tag", tag);
        return articleTagDao.count("SELECT COUNT(*) FROM ArticleTag  i WHERE i.tag=:tag", map);  //
    }

    @Override
    public Article getArticle(long article) {
        return articleDao.select(article);  //
    }

    @Override
    public ArticleTag getArticleTag(long article, long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        map.put("tag", tag);
        return articleTagDao.select("SELECT i FROM ArticleTag i WHERE i.article=:article AND i.tag=:tag", map);
    }

    @Override
    public void delArticle(long article) {
        articleDao.delete(article);
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        articleTagDao.delete("DELETE ArticleTag  i WHERE i.article=:article", map);
        commentDao.delete("DELETE Comment  i WHERE i.article=:article", map);
    }

    @Override
    public List<Article> listArticle() {
        return articleDao.list();  //
    }

    @Override
    public List<Article> latestArticle(int count) {
        return articleDao.list(articleDao.hqlListAll(), null, count);  //
    }

    @Override
    public List<Article> hotArticle(int count) {
        return articleDao.list("FROM Article  i ORDER BY i.visits DESC", null, count);  //;  //
    }

    @Override
    public List<Article> listArticle(int no, int size) {
        return articleDao.list(no, size, articleDao.hqlListAll(), null);  //
    }

    @Override
    public List<Article> listArticleByMonth(int year, int month) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().secondOfDay().withMinimumValue().toDate());
        map.put("end", new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate());
        return articleDao.list("FROM Article  i WHERE i.created>=:begin AND i.created <=:end", map);
    }

    @Override
    public List<Article> listArticleByAuthor(long author) {
        Map<String, Object> map = new HashMap<>();
        map.put("author", author);
        return articleDao.list("FROM Article  i WHERE i.author=:author ORDER BY i.id DESC", map);  //
    }

    @Override
    public List<Article> listArticleByTag(long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("tag", tag);

        List<Article> articles = new ArrayList<>();
        for (ArticleTag at : articleTagDao.list("FROM ArticleTag  i WHERE i.tag=:tag ORDER BY i.article DESC", map)) {
            articles.add(articleDao.select(at.getArticle()));
        }
        return articles;  //
    }

    @Override
    public List<ArticleTag> listArticleTag() {
        return articleTagDao.list();
    }

    @Override
    public Long addArticle(long author, String logo, String title, String summary, String body) {
        Article a = new Article();
        a.setAuthor(author);
        a.setLogo(logo);
        a.setTitle(title);
        a.setSummary(summary);
        a.setBody(body);
        a.setVisits(0l);
        a.setCreated(new Date());
        a.setState(Article.State.PROTECTED);
        return articleDao.persist(a);
    }

    @Override
    public void setArticleAuthor(long article, long user) {
        Map<String,Object> map = new HashMap<>();
        map.put("id", article);
        map.put("author", user);
        articleDao.update("UPDATE Article a SET a.author=:author WHERE a.id=:id ", map);
    }

    @Override
    public void setArticleState(long article, Article.State state) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", article);
        map.put("state", state);
        articleDao.update("UPDATE Article  i SET i.state=:start WHERE i.id=:id", map);
    }

    @Override
    public void setArticle(long id, String logo, String title, String summary, String body) {
        Article a = articleDao.select(id);
        a.setTitle(title);
        a.setLogo(logo);
        a.setSummary(summary);
        a.setBody(body);
        a.setLastEdit(new Date());
        articleDao.update(a);
    }

    @Override
    public void bindArticleTag(long article, long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        map.put("tag", tag);
        ArticleTag at = articleTagDao.select("FROM ArticleTag  i WHERE i.tag=:tag AND i.article=:article", map);

        if (at == null) {
            at = new ArticleTag();
            at.setArticle(article);
            at.setTag(tag);
            articleTagDao.insert(at);
        }

    }

    @Override
    public void setArticleVisits(long article) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", article);
        articleDao.update("UPDATE Article  i SET i.visits=i.visits+1 WHERE i.id=:id", map);
    }

    @Override
    public void setUserVisits(long user) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", user);
        articleDao.update("UPDATE User  i SET i.visits=i.visits+1 WHERE i.id=:id", map);

    }

    @Resource
    private UserDao userDao;
    @Resource
    private ArticleDao articleDao;
    @Resource
    private TagDao tagDao;
    @Resource
    private CommentDao commentDao;
    @Resource
    private ArticleTagDao articleTagDao;

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public void setArticleTagDao(ArticleTagDao articleTagDao) {
        this.articleTagDao = articleTagDao;
    }
}
