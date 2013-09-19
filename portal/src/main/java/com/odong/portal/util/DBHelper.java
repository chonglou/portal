package com.odong.portal.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Log;
import com.odong.portal.entity.User;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.UnsupportedOptionsException;
import org.tukaani.xz.XZOutputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

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
            case "MySQL":
                return jdbcTemplate.queryForObject(String.format("SELECT concat(round(sum(DATA_LENGTH/1024/1024),2),'MB') as data FROM information_schema.TABLES WHERE table_schema='%s'", dbName), String.class);

        }
        return "未知大小";
    }

    public void export() {
        String fileName = appStoreDir + "/backup/" + appName + "_" + format.format(new Date()) + ".json";

        
        LZMA2Options options = new LZMA2Options();
        try {
            options.setPreset(7);    
        }
        catch (UnsupportedOptionsException e){
            logger.error("不支持的压缩选项", e);
        }

        ObjectMapper mapper  = new ObjectMapper();
        try (OutputStream out = new XZOutputStream(new BufferedOutputStream(new FileOutputStream(fileName + ".xz")), options)) {
            Consumer<Object> consumer = (obj)->{
                try {
                    out.write(mapper.writeValueAsBytes(obj));
                    out.write('\n');
                }
                catch (IOException e){
                    logger.error("JSON IO出错", e);
                }
            };
            out.write("Users\n".getBytes());
            accountService.listUser().forEach(consumer);
            out.write("Logs\n".getBytes());
            logService.list().forEach(consumer);
            out.write("Articles\n".getBytes());
            contentService.listArticle().forEach(consumer);
            out.write("Tags\n".getBytes());
            contentService.listTag().forEach(consumer);
            out.write("ArticleTags\n".getBytes());
            contentService.listArticleTag().forEach(consumer);
            out.write("Comments\n".getBytes());
            contentService.listComment().forEach(consumer);
            out.write("FriendLinks\n".getBytes());
            siteService.listFriendLink().forEach(consumer);
            out.write("Settings\n".getBytes());
            siteService.listSetting().forEach(consumer);

            out.flush();

        } catch ( IOException e) {
            logger.error("导出数据库出错", e);
        }
    }

    /**
     * mysqldump -u user -p database | gzip -9 > database.sql.gz
     * gunzip < database.sql.gz | mysql -u user -p database
     */
    public void backup() {
        switch (databaseProductName) {
            case "MySQL":
                logger.info("开始备份数据库{}@mysql", dbName);
                try {
                    String fileName = appStoreDir + "/backup/" + appName + "_" + format.format(new Date()) + ".sql";

                    Process p = Runtime.getRuntime().exec("mysqldump -u " + username
                            + " -p" + password + " " + dbName
                            + " -r " + fileName);


                    if (p.waitFor() == 0) {
                        logger.info("备份成功,开始压缩文件");
                        zipHelper.compress(fileName, true);
                    } else {
                        logger.error("备份失败");
                    }

                } catch (IOException | InterruptedException e) {
                    logger.error("备份数据库{}@mysql出错", dbName, e);
                }
                return;
        }
        logger.error("不支持备份的数据库类型[{}]", databaseProductName);
    }

    private boolean isTableExist(final String tableName) {
        return jdbcTemplate.execute((Connection c)->{

            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getTables(null, dbName, tableName, new String[]{"TABLE", "VIEW"});
            boolean exist = rs.next();
            rs.close();
            return exist;
        });
    }

    @PostConstruct
    void init() {

        jdbcTemplate.execute((Connection c)->{
            DatabaseMetaData dmd = c.getMetaData();
            databaseProductName = dmd.getDatabaseProductName();
            databaseProductVersion = dmd.getDatabaseProductVersion();
            logger.info("使用数据库[{},{}]", databaseProductName, databaseProductVersion);
            return null;  //
        });


        format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    }


    private String databaseProductName;
    private String databaseProductVersion;
    private DateFormat format;
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ZipHelper zipHelper;
    @Value("${app.store}")
    private String appStoreDir;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.dbName}")
    private String dbName;
    @Value("${app.name}")
    private String appName;
    @Resource
    private ContentService contentService;
    @Resource
    private LogService logService;
    @Resource
    private AccountService accountService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private SiteService siteService;
    private final static Logger logger = LoggerFactory.getLogger(DBHelper.class);

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

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
