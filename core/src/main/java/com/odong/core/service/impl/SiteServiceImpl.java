package com.odong.core.service.impl;

import com.odong.core.service.SiteService;
import com.odong.core.store.JdbcHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by flamen on 13-12-30下午5:22.
 */
@Service("core.siteService")
public class SiteServiceImpl extends JdbcHelper implements SiteService {
    @Override
    public void pop(String key) {
        execute("DELETE FROM settings WHERE key_=?", key);
    }

    @Override
    public void put(String key, Object val) {
        execute(
                count("SELECT COUNT(*) FROM settings WHERE key_=?", key) == 0 ?
                        "INSERT INTO settings(val, key_) VALUES(?, ?)" :
                        "UPDATE settings SET val=?, version=version+1 WHERE key_=?",
                val, key);
    }

    @Override
    public String get(String key) {
        return select("SELECT val FROM settings WHERE key_=?", key);
    }


    @PostConstruct
    void init() {
        install("settings",
                stringIdColumn("key_", 255),
                stringColumn("val", 8000, true),
                dateColumn("created", true),
                versionColumn());
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Value("${jdbc.driver}")
    public void setDriver(String driver) {
        jdbcDriver = driver;
    }
}
