package com.odong.core.service.impl;

import com.odong.core.entity.Log;
import com.odong.core.service.LogService;
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
 * Created by flamen on 13-12-30下午4:54.
 */
@Service("core.logService")
public class LogServiceImpl extends JdbcHelper implements LogService {
    @Override
    public List<Log> list(Long user, long start, int size) {
        return list("SELECT * FROM logs WHERE user_=? AND id<? ORDER BY id DESC", new Object[]{user, start}, size, mapperLog());
    }

    @Override
    public void add(Long user, String message, Log.Type type) {
        execute("INSERT INTO logs(user_, message_, type_, created_) VALUES (?, ?, ?, ?)", user, message, type.toString(), new Date());
    }

    @Override
    public void delete(long id) {
        execute("DELETE FROM logs WHERE id = ?", id);
    }

    @Override
    public int count(Long user) {
        return count("SELECT COUNT(*) FROM logs WHERE user_ = ?", user);
    }

    @PostConstruct
    void init() {
        install("logs",
                longIdColumn(),
                longColumn("user_", false),
                enumColumn("type_"),
                stringColumn("message_", 1024, true, false),
                dateColumn("created_", true));
    }

    private RowMapper<Log> mapperLog() {
        return (ResultSet rs, int i) -> {
            Log log = new Log();
            log.setId(rs.getLong("id"));
            log.setType(Log.Type.valueOf(rs.getString("type_")));
            log.setMessage(rs.getString("message_"));
            log.setCreated(rs.getTimestamp("created_"));
            log.setUser(rs.getLong("user_"));
            return log;
        };
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
