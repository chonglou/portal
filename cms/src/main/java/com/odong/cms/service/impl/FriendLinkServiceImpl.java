package com.odong.cms.service.impl;

import com.odong.cms.entity.FriendLink;
import com.odong.cms.service.FriendLinkService;
import com.odong.core.store.JdbcHelper;
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
@Service("cms.friendLinkService")
public class FriendLinkServiceImpl extends JdbcHelper implements FriendLinkService {

    @Override
    public void addFriendLink(String name, String url, String logo) {
        execute("INSERT INTO friendLinks(name_, url_, logo_, created_) VALUES(?,?,?,?)",
                name, url, logo, new Date());
    }

    @Override
    public void setFriendLink(long id, String name, String url, String logo) {
        execute("UPDATE friendLinks set name_=?, url_=?, logo_=? WHERE id=?",
                name, url, logo, id);
    }

    @Override
    public List<FriendLink> listFriendLink() {
        return list("SELECT * FROM friendLinks", mapper());
    }

    @Override
    public FriendLink getFriendLink(long id) {
        return select("SELECT * FROM friendLinks WHERE id=?", new Object[]{id}, mapper());
    }

    @PostConstruct
    void init() {
        install("friendLinks",
                longIdColumn(),
                stringColumn("name_", 255, true, false),
                stringColumn("url_", 255, true, false),
                stringColumn("logo_", 255, false, false),
                dateColumn("created_", true)
        );
    }

    private RowMapper<FriendLink> mapper() {
        return (ResultSet rs, int i) -> {
            FriendLink fl = new FriendLink();
            fl.setId(rs.getLong("id"));
            fl.setLogo(rs.getString("logo_"));
            fl.setName(rs.getString("name_"));
            fl.setUrl(rs.getString("url_"));
            fl.setCreated(rs.getTimestamp("created_"));
            return fl;
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
