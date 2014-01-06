package com.odong.core.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午12:06.
 */
public abstract class JdbcHelper {


    protected void install(String name, String... columns) {
        if (isTableExist(name)) {
            logger.info("表{}已存在", name);
        } else {
            String sql = create(name, columns);
            logger.info("创建表{}：{}", name, sql);
            jdbcTemplate.execute(sql);
        }

    }

    protected void uninstall(String... tableNames) {
        for (String t : tableNames) {
            String sql = drop(t);
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


    @SuppressWarnings("unchecked")
    protected <PK> PK insert(String sql, Object[] objects, String pkName, Class<PK> clazz) {
        logger.debug(sql);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection connection) -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{pkName});
            for (int i = 0; i < objects.length; i++) {
                ps.setObject(i + 1, objects[i]);
            }
            return ps;
        }, kh);
        return (PK) kh.getKey();
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

    protected <T> List<T> list(String sql, Object[] objects, Class<T> clazz) {
        logger.debug(sql);
        return jdbcTemplate.queryForList(sql, objects, clazz);
    }

    protected <T> List<T> list(String sql, Object[] objects, RowMapper<T> mapper) {
        logger.debug(sql);
        return jdbcTemplate.query(sql, objects, mapper);
    }

    protected int count(String sql) {
        logger.debug(sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    protected <T> T select(String sql, Object[] objects, RowMapper<T> mapper) {
        logger.debug(sql);
        try {
            return jdbcTemplate.queryForObject(sql, objects, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    protected <T> T select(String sql, Object[] objects, Class<T> clazz) {
        logger.debug(sql);
        try {
            return jdbcTemplate.queryForObject(sql, objects, clazz);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

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

    /**
     * ****************************** SQL BEGIN ******************************
     * protected String select(String table, String[]columns, String where){
     * StringBuilder sb  = new StringBuilder();
     * sb.append("SELECT ");
     * if(columns == null){
     * sb.append("*");
     * }
     * else {
     * join(sb, columns, ",");
     * }
     * sb.append(" FROM ");
     * sb.append(table);
     * if(where!=null){
     * sb.append(" ");
     * sb.append(where);
     * }
     * return sb.toString();
     * }
     * protected String update(String table, String[]columns, String where){
     * return update(table, columns, where, false);
     * }
     * protected String update(String table, String[]columns, String where, boolean version){
     * StringBuilder sb = new StringBuilder();
     * sb.append("UPDATE ");
     * sb.append(table);
     * sb.append(" SET ");
     * for(int i=0; i<columns.length;i++){
     * if(i>0){
     * sb.append(",");
     * }
     * sb.append(columns[i]);
     * sb.append("=?");
     * }
     * if(version){
     * sb.append(",version=version+1");
     * }
     * if(where!=null){
     * sb.append(" ");
     * sb.append(where);
     * }
     * return sb.toString();
     * }
     * protected String insert(String table, String...columns){
     * StringBuilder sb = new StringBuilder();
     * sb.append("INSERT INTO ");
     * sb.append(table);
     * sb.append("(");
     * join(sb, columns, ",");
     * sb.append(") VALUES(");
     * repeat(sb, "?", ",", columns.length);
     * sb.append(")");
     * return sb.toString();
     * <p>
     * }
     * <p>
     * protected String count(String table, String where){
     * return "SELECT COUNT(*) FROM "+table + (where == null ? "":" WHERE "+where);
     * }
     * protected String delete(String table, String where){
     * return "DELETE FROM "+table + (where == null ? "":" WHERE "+where);
     * }
     * ******************** SQL END****************************
     */

    protected String drop(String name) {
        return "DROP TABLE " + name;
    }

    protected String create(String table, String... columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(table);
        sb.append("(");
        join(sb, columns, ",");
        sb.append(")");
        return sb.toString();
    }

    protected String charsColumn(String name, int len, boolean notNull) {
        String sql = name + " CHAR(" + len + ")";
        if (notNull) {
            sql += " NOT NULL";
        }
        return sql;
    }

    protected String stringColumn(String name, int len, boolean notNull, boolean unique) {
        String sql = name + " VARCHAR(" + len + ")";
        if (notNull) {
            sql += " NOT NULL";
        }
        if (unique) {
            sql += " UNIQUE";
        }
        return sql;
    }

    protected String booleanColumn(String name) {
        return name + " BOOLEAN NOT NULL";
    }

    protected String dateColumn(String name, boolean notNull) {
        String sql = name;
        switch (jdbcDriver) {
            case Driver.MYSQL:
                sql += " DATETIME";
                break;
            default:
                sql += " TIMESTAMP";
                break;
        }
        if (notNull) {
            switch (jdbcDriver) {
                case Driver.MYSQL:
                    sql += " NOT NULL DEFAULT '0000-00-00 00:00:00'";
                    break;
                default:
                    sql += " NOT NULL DEFAULT CURRENT_TIMESTAMP";
                    break;
            }
        }
        return sql;
    }

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
            sql += " NOT NULL DEFAULT 0";
        }
        return sql;
    }

    protected String intColumn(String name, boolean notNull) {
        String sql = name + " INT";
        if (notNull) {
            sql += " NOT NULL DEFAULT 0";
        }
        return sql;
    }

    protected String textColumn(String name, boolean notNull) {
        String sql;
        switch (jdbcDriver) {
            case Driver.MYSQL:
                sql = name + " TEXT";
                break;
            default:
                sql = name + " VARCHAR(8000)";
                break;
        }
        if (notNull) {
            sql += " NOT NULL";
        }
        return sql;
    }

    protected String enumColumn(String name) {
        return name + " CHAR(8) NOT NULL";
    }

    private void join(StringBuilder sb, String[] array, String separator) {
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(array[i]);
        }
    }

    private static void repeat(StringBuilder sb, String str, String separator, int size) {

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(str);
        }
    }

    @Value("${jdbc.driver}")
    protected String jdbcDriver;
    private JdbcTemplate jdbcTemplate;
    private final static Logger logger = LoggerFactory.getLogger(JdbcHelper.class);

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }


    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
