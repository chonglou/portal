package com.odong.core.store;

import com.odong.core.util.ZipHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by flamen on 13-12-30下午3:45.
 */
@Component("core.store.db")
public class DbUtil {
    public String size() {
        switch (jdbcDriver) {
            case Driver.MYSQL:
                try (Connection conn = getConnection();
                     Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(String.format("SELECT concat(round(sum(DATA_LENGTH/1024/1024),2),'MB') as data FROM information_schema.TABLES WHERE table_schema='%s'", jdbcName));
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                } catch (SQLException e) {
                    logger.error("查询数据库大小出错");
                }
                break;
        }
        return "未知大小";
    }

    public void backup() {
        switch (jdbcDriver) {
            case Driver.MYSQL:
                logger.info("开始备份数据库{}@mysql", jdbcName);
                try {
                    String fileName = appStore + "/backup/" + appName + "_" + format.format(new Date()) + ".sql";
                    Process p = Runtime.getRuntime().exec(String.format("mysqldump -u %s -p %s %s -r %s", jdbcUsername, jdbcPassword, jdbcName, fileName));
                    if (p.waitFor() == 0) {
                        logger.info("备份成功,开始压缩文件");
                        zipHelper.compress(fileName, true);
                    } else {
                        logger.error("备份失败");
                    }

                } catch (IOException | InterruptedException e) {
                    logger.error("备份数据库{}@mysql出错", jdbcName, e);
                }
                return;
        }
        logger.error("不支持备份的数据库类型[{}]", jdbcDriver);

    }

    @PostConstruct
    void init() throws ClassNotFoundException, SQLException {
        logger.debug("检查数据库[{}]:", jdbcDriver);

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

        format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
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
    private DateFormat format;
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
    @Resource
    private ZipHelper zipHelper;

    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    public void setZipHelper(ZipHelper zipHelper) {
        this.zipHelper = zipHelper;
    }

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
