package com.odong.core.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by flamen on 13-12-30下午3:45.
 */
@Component("core.store.db")
public class DbUtil {

    @PostConstruct
    void init() throws ClassNotFoundException, SQLException {
        logger.debug("检查数据库");

        switch (jdbcDriver) {
            case Driver.MYSQL:
                jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", jdbcHost, jdbcPort, jdbcName);
                break;
            case Driver.DERBY:
                jdbcUrl = "jdbc:derby:" + appStore + "/db;create=true";
                System.setProperty("derby.stream.error.file", "/tmp/.derby." + appName + ".log");
                break;
            default:
                throw new IllegalArgumentException("不支持的数据库[" + jdbcDriver + "]");

        }
        Class.forName(jdbcDriver);
        checkDatabaseExist();

    }


    private void checkDatabaseExist() throws SQLException {
        switch (jdbcDriver) {
            case Driver.MYSQL:
                try (Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d", jdbcHost, jdbcPort), jdbcUsername, jdbcPassword);
                     Statement stat = conn.createStatement()) {
                    String sql = String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET  utf8", jdbcName);
                    logger.debug(sql);
                    stat.executeUpdate(sql);
                }
                break;

        }
    }

    private Connection getConnection() throws SQLException {
        switch (jdbcDriver) {
            case Driver.DERBY:
                return DriverManager.getConnection(jdbcUrl);
            case Driver.MYSQL:
                return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
            default:
                throw new IllegalArgumentException("不支持的数据库[" + jdbcDriver + "]");

        }
    }


    private String jdbcUrl;
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
    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

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


    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }
}
