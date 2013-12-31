package com.odong.core.store;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午12:06.
 */
public abstract class JdbcHelper {
    protected String stringIdColumn(String name, int len) {
        return name + " VARCHAR(" + len + ") NOT NULL PRIMARY KEY";
    }

    protected String versionColumn() {
        return "version BIGINT NOT NULL DEFAULT 0";
    }

    protected String longIdColumn() {
        switch (jdbcDriver) {
            case Driver.DERBY:
                return "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY";
            default:
                return "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY";
        }
    }

    protected String longColumn(String name, boolean notNull) {
        String sql = name + " BIGINT";
        if (notNull) {
            sql += " NOT NULL";
        }
        return sql;
    }

    protected String stringColumn(String name, int len, boolean notNull) {
        String sql = name + " VARCHAR(" + len + ")";
        if (notNull) {
            sql += " NOT NULL";
        }
        return sql;
    }

    protected String dateColumn(String name, boolean notNull) {
        String sql = name + " TIMESTAMP";
        if (notNull) {
            sql += " NOT NULL DEFAULT CURRENT_TIMESTAMP";
        }
        return sql;
    }

    protected void install(String name, String... columns) {
        if (isTableExist(name)) {
            logger.info("表{}已存在", name);
        } else {
            String sql = String.format("CREATE TABLE %s(%s)", name, StringUtils.join(columns, ","));
            logger.info("创建表{}：{}", name, sql);
            jdbcTemplate.execute(sql);
        }

    }

    protected void uninstall(String... tableNames) {
        for (String t : tableNames) {
            String sql = "DROP TABLE " + t;
            logger.info("删除表{}:{}", t, sql);
            jdbcTemplate.execute(sql);
        }
    }


    protected boolean isTableExist(String tableName) {
        return jdbcTemplate.execute((Connection connection) -> {
            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                if (
                        tableName.equals(name) ||
                                (jdbcDriver.equals(Driver.DERBY) && tableName.equalsIgnoreCase(name))
                        ) {
                    return true;
                }
            }
            boolean exist = rs.next();
            rs.close();
            return exist;
        });
    }

    protected void execute(String sql) {
        logger.debug(sql);
        jdbcTemplate.update(sql);
    }

    protected void execute(String sql, Object... objects) {
        logger.debug(sql);
        jdbcTemplate.update(sql, objects);
    }

    protected <T> List<T> list(String sql, Object[] objects, int size, RowMapper<T> mapper) {
        logger.debug(sql);
        return jdbcTemplate.query(pageStatementCreator(sql, objects, size), mapper);
    }

    protected <T> List<T> list(String sql, RowMapper<T> mapper) {
        logger.debug(sql);
        return jdbcTemplate.query(sql, mapper);
    }

    protected <T> List<T> list(String sql, Object[] objects, RowMapper<T> mapper) {
        logger.debug(sql);
        return jdbcTemplate.query(sql, objects, mapper);
    }

    protected int count(String sql) {
        logger.debug(sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    protected String select(String sql, Object...objects){
        logger.debug(sql);
        return jdbcTemplate.queryForObject(sql, objects, String.class);
    }

    protected int count(String sql, Object... objects) {
        logger.debug(sql);
        return jdbcTemplate.queryForObject(sql, objects, Integer.class);
    }

    protected PreparedStatementCreator pageStatementCreator(String sql, Object[] objects, int maxRows) {
        return (Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            ps.setMaxRows(maxRows);
            return ps;
        };
    }

    protected String jdbcDriver;
    protected JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
}
