package com.odong.portal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午1:18
 */
@Component("config.database")
public class Database {
    public enum Type {
        MYSQL, DERBY
    }

    @PostConstruct
    synchronized void init() throws ClassNotFoundException, SQLException {

        switch (driver) {
            case "com.mysql.jdbc.Driver":
                type = Type.MYSQL;
                url = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);

                logger.info("使用mysql数据库，如果数据库[{}]不存在，将会自动创建", dbName);
                Class.forName(driver);
                try (Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d", host, port), username, password);
                     Statement stat = conn.createStatement()) {
                    stat.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s CHARACTER SET  utf8", dbName));
                }
                return;
            case "org.apache.derby.jdbc.EmbeddedDriver":
                url = String.format("jdbc:derby:var/db/%s;create=true", dbName);
                type = Type.DERBY;

                String log = "var/log";
                File file = new File(log);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        logger.error("创建日志目录失败");
                    }
                }
                System.setProperty("derby.stream.error.file", log + "/derby.log");
                return;
        }
        logger.warn("尚不支持[{}]自动创建,请自行确保数据库[{}]存在", driver, dbName);

    }

    private Type type;
    private String url;
    @Value("${jdbc.host}")
    private String host;
    @Value("${jdbc.port}")
    private int port;
    @Value("${jdbc.dbName}")
    private String dbName;
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    private final static Logger logger = LoggerFactory.getLogger(Database.class);


    public Type getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getDbName() {
        return dbName;
    }

    public String getPassword() {
        return password;
    }

}
