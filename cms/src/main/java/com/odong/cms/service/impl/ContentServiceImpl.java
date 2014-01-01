package com.odong.cms.service.impl;

import com.odong.cms.entity.Article;
import com.odong.cms.entity.ArticleTag;
import com.odong.cms.entity.Comment;
import com.odong.cms.entity.Tag;
import com.odong.cms.service.ContentService;
import com.odong.core.store.JdbcHelper;
import com.odong.core.util.TimeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午6:08.
 */
@Service("cms.contentService")
public class ContentServiceImpl extends JdbcHelper implements ContentService {

    @Override
    public int countTag() {
        return count("SELECT COUNT(*) FROM tags");
    }

    @Override
    public int countTag(long article) {
        return count("SELECT COUNT(*) FROM articleTags WHERE article_=?", article);
    }

    @Override
    public List<Tag> hotTag(int count) {
        return list("SELECT * FROM tags ORDER BY visits_ DESC", new Object[]{}, count, mapperTag());
    }

    @Override
    public List<Tag> listTag() {
        return list("SELECT * FROM tags", mapperTag());
    }

    @Override
    public List<Tag> listTagByArticle(long article) {
        return list("SELECT * FROM tags WHERE id IN (SELECT tag_ FROM articleTags WHERE article_=?)",
                new Object[]{article}, mapperTag());
    }

    @Override
    public Tag getTag(long id) {
        return select("SELECT * FROM tags WHERE id=?", new Object[]{id}, mapperTag());
    }

    @Override
    public Tag getTag(String name) {
        return select("SELECT * FROM tags WHERE name_=?", new Object[]{name}, mapperTag());
    }

    @Override
    public Long addTag(String name, boolean keep) {
        return insert("INSERT INTO tags(name_, keep_, created_) VALUES(?,?,?)",
                new Object[]{name, keep, new Date()}, "id", Long.class);
    }

    @Override
    public void setTagName(long id, String name) {
execute("UPDATE tags SET name_=?,version=version+1 WHERE id=?", name, id);
    }

    @Override
    public void setTagVisits(long tag) {
        execute("UPDATE tags SET visits_=visits_+1 WHERE id=?", tag);
    }

    @Override
    public void delTag(long id) {
        execute("DELETE FROM tags WHERE id=?", id);
    }



    @Override
    public int countComment() {
        return count("SELECT COUNT(*) FROM comments");
    }

    @Override
    public int countCommentByUser(long user) {
        return count("SELECT COUNT(*) FROM comments WHERE user=?", user);
    }

    @Override
    public int countCommentByArticle(long article) {
        return count("SELECT COUNT(*) FROM comments WHERE article_=?", article);
    }

    @Override
    public Comment getComment(long comment) {
        return select("SELECT * FROM comments WHERE id=?",new Object[] {comment}, mapperComment());
    }

    @Override
    public List<Comment> listComment() {
        return list("SELECT * FROM comments", mapperComment());
    }

    @Override
    public List<Comment> listComment(long start, int size) {
        return list("SELECT * FROM comments WHERE id<=? ORDER BY id DESC", new Object[]{start}, size,  mapperComment());
    }

    @Override
    public List<Comment> listCommentByArticle(long article) {
        return list("SELECT * FROM comments WHERE article_=?", new Object[]{article},  mapperComment());
    }

    @Override
    public List<Comment> listCommentByUser(long user) {
        return list("SELECT * FROM comments WHERE user_=?", new Object[]{user},  mapperComment());
    }

    @Override
    public List<Comment> latestComment(int count) {
        return list("SELECT * FROM comments ORDER BY id DESC", new Object[]{}, count, mapperComment());
    }

    @Override
    public void editComment(long comment, String content) {
        execute("UPDATE comments SET content_=?, lastEdit=?,version=version+1 WHERE id=?", content, comment);
    }

    @Override
    public void addComment(Long user, long article, Long comment, String content) {
    execute("INSERT INTO comments(user_, article_, comment_, content_, created_) VALUES(?,?,?,?,?)",
            user, article, comment, comment);
    }

    @Override
    public void delComment(long id) {
        execute("DELETE FROM comments WHERE id=?" , id);
    }

    @Override
    public int countArticle() {
        return count("SELECT COUNT(*) FROM articles");
    }

    @Override
    public int countArticleByAuthor(long author) {
        return count("SELECT COUNT(*) FROM articles WHERE author_=?", author);
    }

    @Override
    public int countArticleByTag(long tag) {
        return count("SELECT COUNT(*) FROM articleTags WHERE tag_=?", tag);
    }

    @Override
    public Article getArticle(long article) {
        return select("SELECT * from articles WHERE id=?", new Object[]{article}, mapperArticle());
    }


    @Override
    public void delArticle(long article) {
        execute("DELETE FROM articles WHERE id=?", article);
    }

    @Override
    public List<Article> listArticle() {
        return list("SELECT * FROM articles", mapperArticle());
    }

    @Override
    public List<Article> latestArticle(int count) {
        return list("SELECT * FROM articles ORDER BY id DESC", new Object[]{}, count, mapperArticle());
    }

    @Override
    public List<Article> hotArticle(int count) {
        return list("SELECT * FROM articles ORDER BY visits DESC", new Object[]{}, count, mapperArticle());
    }

    @Override
    public List<Article> listArticle(long start, int size) {
        return list("SELECT * FROM articles WHERE id<=? ORDER BY id DESC", new Object[]{start}, size, mapperArticle());
    }



    @Override
    public List<Article> listArticleByAuthor(long author) {
        return list("SELECT * FROM articles WHERE author_=?", new Object[]{author}, mapperArticle());
    }

    @Override
    public List<Article> listArticleByTag(long tag) {
        return list("SELECT * FROM articles WHERE id IN (SELECT article_ FROM articleTags WHERE tag_=?)",
                new Object[]{tag}, mapperArticle());
    }

    @Override
    public List<Long> getTagIdsByArticle(long article) {
        return list("SELECT tag_ FROM articleTags WHERE article_=? ", new Object[]{article}, Long.class);
    }

    @Override
    public List<Long> getArticleIdsByTag(long tag) {
        return list("SELECT article_ FROM articleTags WHERE tag_=? ", new Object[]{tag}, Long.class);
    }

    @Override
    public List<Long> getArticleIdsByPage(long start, int size) {
        return list("SELECT id FROM articles WHERE id<=?  ORDER BY id DESC", new Object[]{start}, size, mapperId() );
    }

    @Override
    public List<Long> getArticleIdsByAuthor(long user) {
        return list("SELECT id FROM articles WHERE author_=?", new Object[]{user}, Long.class);
    }

    @Override
    public List<Long> getArticleIdsByCreated(Date begin, Date end) {
        return list("SELECT id FROM articles WHERE created_>=? AND created_<=?", new Object[]{begin, end}, Long.class);
    }

    @Override
    public List<Long> getArticleIdsBySearch(String key) {
        return list("SELECT id FROM articles WHERE id LIKE ?", new Object[]{"%"+key+"%"}, Long.class);
    }

    @Override
    public long addArticle(long author, String logo, String title, String summary, String body) {
        return insert("INSERT INTO articles(author_, logo_, title_, summary_, body_, created_)",
                new Object[]{author, logo, title, summary, body, new Date()},
                "id", Long.class
        );
    }

    @Override
    public void setArticleAuthor(long article, long user) {
        execute("UPDATE article SET author_=? WHERE id=?", user, article);
    }

    @Override
    public void setArticle(long id, String logo, String title, String summary, String body) {
        execute("UPDATE articles SET logo_=?, title_=?, summary_=?, body_=?, lastEdit_=?,version=version+1 WHERE id=?",
                logo, title, summary, body, new Date(), id);
    }

    @Override
    public void bindArticleTag(long article, long tag, boolean bind) {
        if(bind){
            Long id = select("SELECT id FROM articleTags WHERE article_=? AND tag_=?", new Object[]{article, tag},Long.class);
            if(id == null){
                execute("INSERT INTO articleTags(article_, tag_, created_) VALUES(?,?,?)",
                        article, tag, new Date());
            }
        }
        else {
            execute("DELETE FROM articleTags WHERE article_=? AND tag_=?", article, tag);
        }

    }

    @Override
    public void setArticleVisits(long article) {
        execute("UPDATE articles SET visits_=visits_+1 WHERE id=?", article);
    }

    @Override
    public void setUserVisits(long user) {
        execute("UPDATE users SET visits_=visits_+1 WHERE id=?", user);
    }

    @PostConstruct
    void init() {
        install("articles",
                longIdColumn(),
                stringColumn("title_", 255, true, false),
                stringColumn("summary_", 500, false, false),
                stringColumn("logo_", 255, false, false),
                textColumn("body_", true),
                longColumn("author_", false),
                dateColumn("lastEdit_",false),
                longColumn("visits_", true),
                dateColumn("created_", true),
                versionColumn()
        );
        install("tags",
                longIdColumn(),
                stringColumn("name_", 255, true,true),
                longColumn("visits_", true),
                dateColumn("lastEdit_", false),
                dateColumn("created_", true),
                booleanColumn("keep_"),
                versionColumn()
        );
        install("articleTags",
                longIdColumn(),
                longColumn("article_", true),
                longColumn("tag_", true),
                dateColumn("created_", true)
        );

        install("comments",
                longIdColumn(),
                longColumn("user_", true),
                longColumn("comment", false),
                stringColumn("content_", 1024, true, false),
                dateColumn("lastEdit_", false),
                dateColumn("created_", true),
                versionColumn()
        );
    }

    RowMapper<Long> mapperId(){
        return (ResultSet rs, int i) -> rs.getLong("id");

    }
    RowMapper<Comment> mapperComment(){
        return (ResultSet rs, int i) -> {
            Comment c = new Comment();
            c.setId(rs.getLong("id"));
            c.setUser(rs.getLong("user_"));
            c.setComment(rs.getLong("comment_"));
            c.setContent(rs.getString("content_"));
            c.setLastEdit(rs.getTimestamp("lastEdit_"));
            c.setCreated(rs.getTimestamp("created_"));
            c.setVersion(rs.getInt("version"));
            return c;
        };
    }

    RowMapper<Article> mapperArticle(){
        return (ResultSet rs, int i) -> {
            Article a = new Article();
            a.setId(rs.getLong("id"));
            a.setTitle(rs.getString("title_"));
            a.setSummary(rs.getString("summary_"));
            a.setLogo(rs.getString("logo_"));
            a.setBody(rs.getString("body_"));
            a.setAuthor(rs.getLong("author_"));
            a.setLastEdit(rs.getTimestamp("lastEdit_"));
            a.setVisits(rs.getLong("visits_"));
            a.setCreated(rs.getTimestamp("created_"));
            a.setVersion(rs.getInt("version"));
            return a;
        };
    }
    RowMapper<Tag> mapperTag(){
        return (ResultSet rs, int i) -> {
            Tag t = new Tag();
            t.setId(rs.getLong("id"));
            t.setVisits(rs.getLong("visits_"));
            t.setLastEdit(rs.getTimestamp("lastEdit_"));
            t.setCreated(rs.getTimestamp("created_"));
            t.setKeep(rs.getBoolean("keep_"));
            t.setVersion(rs.getInt("version"));
            return t;
        };
    }
    RowMapper<ArticleTag> mapperArticleTag(){
        return (ResultSet rs, int i) -> {
            ArticleTag at = new ArticleTag();
            at.setCreated(rs.getTimestamp("created_"));
            at.setId(rs.getLong("id"));
            at.setTag(rs.getLong("tag_"));
            at.setArticle(rs.getLong("article_"));
            return at;
        };
    }



    @Value("${jdbc.driver}")
    public void setDriver(String driver) {
        jdbcDriver = driver;
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
