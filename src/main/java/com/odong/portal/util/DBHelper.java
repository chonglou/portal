package com.odong.portal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午1:26
 */
@Component
public class DBHelper {
    public String getSize() {
        switch (databaseProductName) {
            case "Mysql":
                return jdbcTemplate.queryForObject(String.format("SELECT concat(round(sum(DATA_LENGTH/1024/1024),2),'MB') as data FROM TABLES WHERE table_schema='%s'", databaseSchema), String.class);

        }
        return "未知大小";
    }

    /*
  mysqldump -u user -p database | gzip -9 > database.sql.gz
  gunzip < database.sql.gz | mysql -u user -p database
     */
    public void backup() {
        switch (databaseProductName) {
            case "Mysql":
                logger.info("开始备份数据库{}@mysql", databaseSchema);
                try {
                    String fileName = appStoreDir + "/backup/" + databaseSchema + "_" + format.format(new java.util.Date()) + ".sql";

                    Process p = Runtime.getRuntime().exec("mysqldump -u " + username
                            + " -p" + password + " " + databaseSchema
                            + " -r " + fileName);


                    if (p.waitFor() == 0) {
                        logger.info("备份成功,开始压缩文件");
                        zipHelper.compress(fileName, true);
                    } else {
                        logger.error("备份失败");
                    }

                } catch (IOException | InterruptedException e) {
                    logger.error("备份数据库{}@mysql出错", databaseSchema, e);
                }
                return;
        }
        logger.error("未知的数据库类型[{}]" + databaseProductName);
    }

    private boolean isTableExist(final String tableName) {

        return jdbcTemplate.execute(new ConnectionCallback<Boolean>() {
            @Override
            public Boolean doInConnection(Connection connection) throws SQLException, DataAccessException {

                DatabaseMetaData dmd = connection.getMetaData();
                ResultSet rs = dmd.getTables(null, databaseSchema, tableName, new String[]{"TABLE", "VIEW"});
                boolean exist = rs.next();
                rs.close();
                return exist;
            }
        });
    }

    @PostConstruct
    void init() {
        jdbcTemplate.execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
                DatabaseMetaData dmd = connection.getMetaData();
                databaseProductName = dmd.getDatabaseProductName();
                databaseProductVersion = dmd.getDatabaseProductVersion();
                databaseSchema = connection.getSchema();
                logger.info("使用数据库[{},{},{}]", databaseProductName, databaseProductVersion, databaseSchema);
                return null;  //
            }
        });

        format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    }

    private String databaseProductName;
    private String databaseProductVersion;
    private String databaseSchema;

    private DateFormat format;
    @Resource
    private ZipHelper zipHelper;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Value("${app.store}")
    private String appStoreDir;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    private final static Logger logger = LoggerFactory.getLogger(DBHelper.class);

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void setZipHelper(ZipHelper zipHelper) {
        this.zipHelper = zipHelper;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

}
