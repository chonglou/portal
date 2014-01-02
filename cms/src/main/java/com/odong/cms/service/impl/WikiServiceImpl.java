package com.odong.cms.service.impl;

import com.odong.cms.entity.Wiki;
import com.odong.cms.service.WikiService;
import com.odong.core.store.JdbcHelper;
import com.odong.web.model.Link;
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
 * Created by flamen on 13-12-31下午7:49.
 */
@Service("cms.wikiService")
public class WikiServiceImpl extends JdbcHelper implements WikiService {

    @Override
    public Wiki getWiki(String name) {
        List<Wiki> list = list("SELECT * FROM wikis WHERE name_=? ORDER BY id DESC", new Object[]{name}, mapperWiki());
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Link> listWiki() {
        return list("SELECT DISTINCT name_, title_ FROM wikis",
                (ResultSet rs, int i) -> new Link(rs.getString("title_"), rs.getString("name_"))
        );
    }

    @Override
    public void setWiki(long user, String name, String title, String body, int version) {
        Long id = select("SELECT id FROM wikis WHERE user_=? AND name_=? AND version=?", new Object[]{user, name, version}, Long.class);
        if (id == null) {
            execute("INSERT INTO wikis(user_, name_, title_, body_, created_, version) VALUES(?,?,?,?,?,?)",
                    user, name, title, body, new Date(), version);
        } else {
            execute("UPDATE wikis SET title_=?, body=? WHERE id=?", title, body, id);
        }
    }

    @PostConstruct
    void init() {
        install("wikis",
                longIdColumn(),
                longColumn("user_", true),
                stringColumn("name_", 255, true, false),
                stringColumn("title_", 255, true, false),
                textColumn("body_", true),
                dateColumn("created_", true),
                versionColumn()
        );
    }

    private RowMapper<Wiki> mapperWiki() {
        return (ResultSet rs, int i) -> {
            Wiki w = new Wiki();
            w.setId(rs.getLong("id"));
            w.setTitle(rs.getString("title_"));
            w.setName(rs.getString("name_"));
            w.setBody(rs.getString("body_"));
            w.setCreated(rs.getTimestamp("created_"));
            w.setVersion(rs.getInt("version"));
            return w;
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
