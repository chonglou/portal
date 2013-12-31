package com.odong.core.service.impl;

import com.odong.core.entity.Log;
import com.odong.core.service.LogService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by flamen on 13-12-30下午4:54.
 */
@Service("core.logService")
public class LogServiceImpl implements LogService {
    @Override
    public List<Log> list(int no, int size) {
        int start = (no - 1) * size;
        if (start < 0) {
            start = 0;
        }
        //jdbcTemplate.query("SELECT * FROM logs ORDER BY ID DESC")
        //FIXME
        return null;
    }

    @Override
    public void add(Long user, String message, Log.Type type) {
        jdbcTemplate.update("INSERT INTO logs(user_, message, type_) VALUES (?, ?, ?)", user, message, type.toString());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM logs WHERE id = ?", id);
    }

    @Override
    public int count(Long user) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM logs WHERE user = ?", Integer.class, user);
    }


    private RowMapper<Log> mapperLog() {
        return (ResultSet rs, int i) -> {
            Log log = new Log();
            log.setId(rs.getLong("id"));
            log.setType(Log.Type.valueOf(rs.getString("type_")));
            log.setMessage(rs.getString("message"));
            log.setCreated(rs.getTimestamp("created"));
            log.setUser(rs.getLong("user_"));
            return log;
        };
    }


    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
