package com.odong.portal.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    public void import4json(String filename) throws IOException {
        Path file = Paths.get(filename);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader reader = Files.newBufferedReader(file, CHARSET)) {

            importSetting(reader.readLine(), mapper);
            importFriendLink(reader.readLine(), mapper);
            importTag(reader.readLine(), mapper);
            importUser(reader.readLine(), mapper);

            //文章列表
            String line;
            while ((line = reader.readLine()) != null) {
               importArticle(line, mapper);
            }
        }
        logger.debug("成功导入数据[{}]", filename);
    }

    private void importArticle(String line, ObjectMapper mapper) throws IOException {
        //文章
        Map<String, String> a = mapper.readValue(line, new TypeReference<Map<String, String>>() {
        });
        long aid = contentService.addArticle(accountService.getUser(a.get("author")).getId(), a.get("logo"), a.get("title"), a.get("summary"), a.get("body"), getDate(a.get("created")), getLong(a.get("visits")));

        List<String> tags = mapper.readValue(a.get("tags"), new TypeReference<List<String>>() {
        });
        for (String t : tags) {
            contentService.bindArticleTag(aid, contentService.getTag(t).getId());
        }

        List<Map<String, String>> comments = mapper.readValue(a.get("comments"), new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String, String> c : comments) {
            contentService.addComment(accountService.getUser(c.get("user")).getId(), aid, c.get("content"), getDate(c.get("created")));
        }
    }

    private void importUser(String line, ObjectMapper mapper) throws IOException {
        //用户
        List<Map<String, String>> users = mapper.readValue(line, new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String,String> u : users){
            User user = accountService.getUser(u.get("email"));
            if(user == null){
                accountService.addUser(u.get("email"), u.get("username"), getLong(u.get("visits")), getDate(u.get("created")));
            }
        }
    }

    private void importTag(String line, ObjectMapper mapper) throws IOException {
        //标签
        List<Map<String, String>> tags = mapper.readValue(line, new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String,String> t : tags){
            Tag tag = contentService.getTag(t.get("name"));
            if(tag == null){
                contentService.addTag(t.get("name"), getBoolean(t.get("keep")), getLong(t.get("visits")), getDate(t.get("created")));
            }
        }
    }

    private void importFriendLink(String line, ObjectMapper mapper) throws IOException {
        //友情链接
        List<Map<String, String>> friendLinks = mapper.readValue(line, new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String, String> fl : friendLinks) {
            siteService.addFriendLink(fl.get("name"), fl.get("url"), fl.get("logo"));
        }
    }

    private void importSetting(String line, ObjectMapper mapper) throws IOException {
        //系统信息
        Map<String, String> env = mapper.readValue(line, new TypeReference<Map<String, String>>() {
        });
        for (String s : env.keySet()) {
            siteService.set("site." + s, env.get(s));
        }
    }

    /**
     * 数据库导出
     *
     * @throws IOException
     */
    public void export2json() throws IOException {
        String fileName = appStoreDir + "/backup/" + appName + "_" + format.format(new Date()) + ".json";
        Path file = Paths.get(fileName);
        ObjectMapper mapper = new ObjectMapper();
        logger.debug("导出数据库到文件", fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(file, CHARSET)) {
            exportSetting(writer, mapper);
            exportFriendLink(writer, mapper);
            exportTag(writer, mapper);
            exportUser(writer, mapper);
            contentService.listArticle().forEach((article) -> {
                try {
                    exportArticle(writer, mapper, article);
                } catch (IOException e) {
                    logger.error("导出文章[{}]出错", article.getId(), e);
                }
            });
        }

        logger.debug("开始压缩文件[{}]", fileName);
        zipHelper.compress(fileName, true);
    }

    private boolean getBoolean(String b){
        return Boolean.valueOf(b);
    }
    private Date getDate(String time) {
        return new Date(getLong(time));
    }

    private long getLong(String str) {
        return Long.parseLong(str);
    }



    private void exportUser(Writer writer, ObjectMapper mapper) throws IOException {
        //用户列表
        List<Map<String, String>> users = new ArrayList<>();
        accountService.listUser().forEach((u) -> {
            Map<String, String> map = new HashMap<>();
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("created", "" + u.getCreated().getTime());
            map.put("visits", "" + u.getVisits());

            users.add(map);
        });
        writer.write(mapper.writeValueAsString(users));
        writer.write('\n');
    }

    private void exportArticle(Writer writer, ObjectMapper mapper, Article article) throws IOException {
        //文章列表
        Map<String, String> map = new HashMap<>();
        map.put("author", accountService.getUser(article.getAuthor()).getEmail());
        map.put("title", article.getTitle());
        map.put("summary", article.getSummary());
        map.put("logo", article.getLogo());
        map.put("body", article.getBody());
        map.put("created", "" + article.getCreated().getTime());
        map.put("visits", "" + article.getVisits());

        List<String> tags = new ArrayList<>();
        contentService.listTagByArticle(article.getId()).forEach((t) -> {
            tags.add(t.getName());
        });
        map.put("tags", mapper.writeValueAsString(tags));

        List<Map<String, String>> comments = new ArrayList<>();
        contentService.listComment().forEach((c) -> {
            Map<String, String> comment = new HashMap<>();
            comment.put("content", c.getContent());
            comment.put("created", "" + c.getCreated().getTime());
            comment.put("user", accountService.getUser(c.getUser()).getEmail());
            comments.add(comment);
        });
        map.put("comments", mapper.writeValueAsString(comments));

        writer.write(mapper.writeValueAsString(map));
        writer.write('\n');

    }

    private void exportTag(Writer writer, ObjectMapper mapper) throws IOException {
        //标签列表
        List<Map<String, String>> tags = new ArrayList<>();
        contentService.listTag().forEach((t) -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", t.getName());
            map.put("visits", "" + t.getVisits());
            map.put("created", "" + t.getCreated().getTime());
            map.put("keep", "" + t.isKeep());
            tags.add(map);
        });
        try {
            writer.write(mapper.writeValueAsString(tags));
            writer.write('\n');
        } catch (IOException e) {
            logger.error("导出标签列表出错", e);
        }
    }

    private void exportSetting(Writer writer, ObjectMapper mapper) throws IOException {
        //站点信息
        Map<String, String> env = new HashMap<>();
        for (String k : new String[]{"domain", "title", "keywords", "description", "copyright", "aboutMe", "regProtocol"}) {
            env.put(k, siteService.getString("site." + k));
        }
        writer.write(mapper.writeValueAsString(env));
        writer.write('\n');
    }

    private void exportFriendLink(Writer writer, ObjectMapper mapper) throws IOException {
        //友情链接
        List<Map<String, String>> friendLinks = new ArrayList<>();
        siteService.listFriendLink().forEach((fl) -> {
            Map<String, String> map = new HashMap<>();
            map.put("url", fl.getUrl());
            map.put("logo", fl.getLogo());
            map.put("name", fl.getName());
            friendLinks.add(map);
        });
        writer.write(mapper.writeValueAsString(friendLinks));
        writer.write('\n');
    }

    public void export2json_all() throws IOException {
        String fileName = appStoreDir + "/backup/" + appName + "_" + format.format(new Date()) + ".json";
        Path file = Paths.get(fileName);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter writer = Files.newBufferedWriter(file, CHARSET)) {
            Consumer<Object> consumer = (obj) -> {
                try {
                    writer.write(mapper.writeValueAsString(obj));
                    writer.write('\n');
                } catch (IOException e) {
                    logger.error("JSON IO出错", e);
                }
            };
            writer.write("Users\n");
            accountService.listUser().forEach(consumer);
            writer.write("Logs\n");
            logService.list().forEach(consumer);
            writer.write("Articles\n");
            contentService.listArticle().forEach(consumer);
            writer.write("Tags\n");
            contentService.listTag().forEach(consumer);
            writer.write("ArticleTags\n");
            contentService.listArticleTag().forEach(consumer);
            writer.write("Comments\n");
            contentService.listComment().forEach(consumer);
            writer.write("FriendLinks\n");
            siteService.listFriendLink().forEach(consumer);
            writer.write("Settings\n");
            siteService.listSetting().forEach(consumer);
        }
        logger.debug("导出数据库到文件[{}]成功", fileName);
        zipHelper.compress(fileName, true);
        logger.debug("压缩文件[{}]", fileName);
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
        return jdbcTemplate.execute((Connection c) -> {

            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getTables(null, dbName, tableName, new String[]{"TABLE", "VIEW"});
            boolean exist = rs.next();
            rs.close();
            return exist;
        });
    }

    @PostConstruct
    void init() {

        jdbcTemplate.execute((Connection c) -> {
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
    private SiteService siteService;
    @Resource
    private StringHelper stringHelper;
    private final Charset CHARSET = Charset.forName("UTF-8");
    private final static Logger logger = LoggerFactory.getLogger(DBHelper.class);

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

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
