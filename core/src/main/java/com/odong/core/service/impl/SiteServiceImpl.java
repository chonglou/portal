package com.odong.core.service.impl;

import com.odong.core.service.SiteService;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by flamen on 13-12-30下午5:22.
 */
public class SiteServiceImpl implements SiteService {
    @Override
    public void pop(String key) {
        jdbcTemplate.update("DELETE FROM settings WHERE key_=?", key);
    }

    @Override
    public void put(String key, Object val) {
        jdbcTemplate.update(
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM settings WHERE key_=?", new Object[]{key},
                        Integer.class) == 0 ?
                        "INSERT INTO settings(val, key_) VALUES(?, ?)" :
                        "UPDATE settings SET val=? version=version+1 WHERE key_=?", val, key);
    }

    @Override
    public String get(String key) {
        return jdbcTemplate.queryForObject("SELECT val FROM settings WHERE key_=?", new Object[]{key}, String.class);
    }


    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
