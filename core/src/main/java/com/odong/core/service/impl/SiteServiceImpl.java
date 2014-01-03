package com.odong.core.service.impl;

import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import com.odong.core.service.SiteService;
import com.odong.core.store.JdbcHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Date;

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
    public void set(String key, Serializable obj) {
        set(key, obj, false);
    }

    @Override
    public void set(String key, Serializable obj, boolean encrypt) {
        String json = jsonHelper.object2json(obj);
        set(key, encrypt ? encryptHelper.encode(json) : json);
    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz) {
        return get(key, clazz, false);
    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz, boolean encrypt) {
        String json = get(key);
        return jsonHelper.json2object(encrypt ? encryptHelper.decode(json) : json, clazz);
    }

    private void set(String key, String val) {
        if (count("SELECT COUNT(*) FROM settings WHERE key_=?", key) == 0) {
            execute("INSERT INTO settings(key_, val_, created_) VALUES(?, ?, ?)", key, val, new Date());
        } else {
            execute("UPDATE settings SET val_=?, version=version+1 WHERE key_=?", val, key);
        }
    }

    private String get(String key) {
        return select("SELECT val_ FROM settings WHERE key_=?", new Object[]{key}, String.class);
    }


    @PostConstruct
    void init() {
        install("settings",
                stringIdColumn("key_", 255),
                textColumn("val_", true),
                dateColumn("created_", true),
                versionColumn());
    }


    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private EncryptHelper encryptHelper;

    @Value("${jdbc.driver}")
    public void setDriver(String driver) {
        jdbcDriver = driver;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }


    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}