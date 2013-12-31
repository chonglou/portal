package com.odong.core.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-30下午3:45.
 */
@Component("core.util.jdbc")
public class JdbcUtil {
    public enum Type {
        DERBY, MYSQL
    }

    public void install(Map<String, String> map) {
        try (Connection c = getConnection();
             Statement s = c.createStatement()) {
            for (String t : map.keySet()) {
                if (isTableExist(t)) {
                    logger.info("表{}已存在", t);
                } else {
                    logger.info("创建表{}", t);
                    s.executeUpdate(String.format("CREATE TABLE %s(%s)", t, map.get(t)));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("创建表出错", e);
        }
    }

    public void uninstall(String... tables) {
        try (Connection c = getConnection();
             Statement s = c.createStatement()) {
            for (String t : tables) {
                logger.info("删除表{}", t);
                s.executeUpdate("DROP TABLE " + t);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("删除表出错", e);
        }
    }


    @PostConstruct
    void init() throws ClassNotFoundException, SQLException {
        logger.debug("检查数据库");

        switch (jdbcDriver) {
            case "com.mysql.jdbc.Driver":
                type = Type.MYSQL;
                jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", jdbcHost, jdbcPort, jdbcName);
                break;
            case "org.apache.derby.jdbc.EmbeddedDriver":
                type = Type.DERBY;
                jdbcUrl = "jdbc:derby:" + appStore + "/db;create=true";
                System.setProperty("derby.stream.error.file", "/tmp/.derby." + appName + ".log");
                break;
            default:
                throw new IllegalArgumentException("不支持的数据库[" + jdbcDriver + "]");

        }
        Class.forName(jdbcDriver);
        checkDatabaseExist();

        Map<String, String> map = new HashMap<>();
        map.put("logs",
                idColumn()
                        + "user_ BIGINT,"
                        + "type_ VARCHAR(255) NOT NULL, "
                        + "message VARCHAR(1024) NOT NULL, "
                        + "created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");

        map.put("settings", "key_ VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "val VARCHAR(8000) NOT NULL, " +
                "created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "version BIGINT NOT NULL DEFAULT 0");
        install(map);
    }

    private String idColumn() {
        switch (type) {
            case DERBY:
                return "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, ";
            default:
                return "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, ";
        }
    }

    private boolean isTableExist(String tableName) throws SQLException {
        try (Connection c = getConnection()) {
            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getTables(null, null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                if (
                        tableName.equals(name) ||
                                (type == Type.DERBY && tableName.equalsIgnoreCase(name))
                        ) {
                    return true;
                }
            }
            boolean exist = rs.next();
            rs.close();
            return exist;
        }
    }

    private void checkDatabaseExist() throws SQLException {
        switch (type) {
            case MYSQL:
                try (Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d", jdbcHost, jdbcPort), jdbcUsername, jdbcPassword);
                     Statement stat = conn.createStatement()) {
                    stat.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET  utf8", jdbcName));
                }
                break;

        }
    }

    private Connection getConnection() throws SQLException {
        switch (type) {
            case DERBY:
                return DriverManager.getConnection(jdbcUrl);
            case MYSQL:
                return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            default:
                throw new IllegalArgumentException("不支持的数据库[" + jdbcDriver + "]");

        }
    }


    private String jdbcUrl;
    private Type type;
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.host}")
    private String jdbcHost;
    @Value("${jdbc.port}")
    private int jdbcPort;
    @Value("${jdbc.name}")
    private String jdbcName;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;
    @Value("${app.name}")
    private String appName;
    @Value("${app.store}")
    private String appStore;
    private final static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public void setJdbcHost(String jdbcHost) {
        this.jdbcHost = jdbcHost;
    }

    public void setJdbcPort(int jdbcPort) {
        this.jdbcPort = jdbcPort;
    }

    public void setJdbcName(String jdbcName) {
        this.jdbcName = jdbcName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppStore(String appStore) {
        this.appStore = appStore;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public Type getType() {
        return type;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }
}
