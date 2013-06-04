package com.odong.portal.config;

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
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午1:18
 */
@Component("database.init")
public class Database {

    @PostConstruct
    void init() throws ClassNotFoundException, SQLException {
        if ("com.mysql.jdbc.Driver".equals(driver)) {
            String db = driver.split("/")[3];
            logger.info("使用mysql jdbc驱动，如果数据库[{}]不存在，将会自动创建", db);
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, usernam, password);
                 Statement stat = conn.createStatement()) {
                stat.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db + " CHARACTER SET  utf8");
            }
        }

    }

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String usernam;
    @Value("${jdbc.password}")
    private String password;
    private final static Logger logger = LoggerFactory.getLogger(Database.class);

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
