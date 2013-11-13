package com.odong.portal.job;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.Task;
import com.odong.portal.entity.User;
import com.odong.portal.model.profile.QrCodeProfile;
import com.odong.portal.model.profile.SmtpProfile;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheService;
import com.odong.portal.util.DBHelper;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.JsonHelper;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午7:56
 */
@Component("taskListener")
public class TaskListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            text((TextMessage) message);

        } else if (message instanceof MapMessage) {
            map((MapMessage) message);
        } else {
            logger.error("未知的消息类型");
        }
    }

    @SuppressWarnings("unchecked")
    private void map(MapMessage message) {
        try {
            Task.Type type = Task.Type.valueOf(message.getStringProperty("type"));
            String itemId = message.getJMSCorrelationID();
            switch (type) {
                case IMPORT:
                    taskExecutor.execute(() -> {
                        try {
                            dbHelper.import4json(message.getString("filename"));
                        } catch (Exception e) {
                            logger.error("导入数据出错", e);
                        }
                    });
                    break;
                case EXPORT:
                    taskExecutor.execute(() -> {
                        try {
                            dbHelper.export2json();
                        } catch (Exception e) {
                            logger.error("导出数据出错", e);
                        }
                    });

                    break;
                case GC:
                    System.gc();
                    break;
                case BACKUP:
                    taskExecutor.execute(() -> {
                        dbHelper.backup();
                        siteService.set("site.lastBackup", new Date());
                    });
                    break;
                case RSS:
                    taskExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            try (Writer writer = new FileWriter(appStoreDir + "/seo/rss.xml")) {
                                String domain = "http://" + siteService.getString("site.domain");
                                SyndFeed feed = new SyndFeedImpl();
                                feed.setFeedType("rss_2.0");
                                feed.setTitle(siteService.getString("site.title"));
                                feed.setLink(domain);
                                feed.setDescription(siteService.getString("site.description"));
                                List<SyndEntry> entries = new ArrayList<>();

                                entries.add(createRssSyndEntry(domain, "/aboutMe", "关于我们", siteService.getString("site.about_me"), siteService.getDate("site.init")));

                                for (Article a : contentService.listArticle()) {
                                    entries.add(createRssSyndEntry(domain, "/article/" + a.getId(), a.getTitle(), a.getSummary(), a.getPublishDate()));
                                }

                                feed.setEntries(entries);
                                SyndFeedOutput output = new SyndFeedOutput();
                                output.output(feed, writer);
                            } catch (IOException | FeedException e) {
                                logger.error("生成RSS出错", e);
                            }
                        }

                        private SyndEntry createRssSyndEntry(String domain, String url, String title, String body, Date publishedDate) {
                            SyndEntry entry = new SyndEntryImpl();
                            entry.setTitle(title);
                            entry.setLink(domain + url);
                            entry.setPublishedDate(publishedDate);
                            SyndContent aboutDesc = new SyndContentImpl();
                            aboutDesc.setType("text/html");
                            aboutDesc.setValue(body);
                            entry.setDescription(aboutDesc);
                            return entry;
                        }
                    });
                    break;
                case SITE_MAP:
                    taskExecutor.execute(() -> {
                        try {
                            String domain = "http://" + siteService.getString("site.domain");
                            WebSitemapGenerator wsg = WebSitemapGenerator.builder(domain, new File(appStoreDir + "/seo/")).gzip(true).build();
                            wsg.addUrl(new WebSitemapUrl.Options(domain + "/main").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build());
                            wsg.addUrl(new WebSitemapUrl.Options(domain + "/aboutMe").lastMod(siteService.getDate("site.init")).priority(0.9).changeFreq(ChangeFreq.YEARLY).build());
                            wsg.addUrl(new WebSitemapUrl.Options(domain + "/sitemap").lastMod(new Date()).priority(0.9).changeFreq(ChangeFreq.DAILY).build());
                            for (Article a : contentService.listArticle()) {
                                wsg.addUrl(new WebSitemapUrl.Options(domain + "/article/" + a.getId()).lastMod(a.getPublishDate()).priority(0.5).changeFreq(ChangeFreq.MONTHLY).build());
                            }
                            for (Tag t : contentService.listTag()) {
                                wsg.addUrl(new WebSitemapUrl.Options(domain + "/tag/" + t.getId()).lastMod(t.getPublishDate()).priority(0.7).changeFreq(ChangeFreq.WEEKLY).build());
                            }
                            for (User u : accountService.listUser()) {
                                wsg.addUrl(new WebSitemapUrl.Options(domain + "/user/" + u.getId()).lastMod(u.getCreated()).priority(0.7).changeFreq(ChangeFreq.WEEKLY).build());
                            }
                            DateTime now = new DateTime();
                            for (DateTime dt = new DateTime(siteService.getDate("site.init")); dt.compareTo(now) <= 0; dt = dt.plusMonths(1)) {
                                wsg.addUrl(new WebSitemapUrl.Options(domain + "/archive/" + dt.toString("yyyy/MM")).lastMod(dt.toDate()).priority(0.2).changeFreq(ChangeFreq.MONTHLY).build());
                            }

                            wsg.write();
                        } catch (IOException e) {
                            logger.error("创建网站地图出错", e);
                        }
                    });
                    break;
                case QR_CODE:
                    taskExecutor.execute(() -> {
                        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
                        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                        QrCodeProfile qcp = siteService.getObject("site.qrCode", QrCodeProfile.class);
                        try {

                            BitMatrix matrix = new MultiFormatWriter().encode(
                                    qcp.getContent(),
                                    BarcodeFormat.QR_CODE,
                                    qcp.getWidth(),
                                    qcp.getHeight(),
                                    hints);
                            MatrixToImageWriter.writeToFile(matrix,
                                    "png",
                                    new File(appStoreDir + "/site.png"));
                        } catch (IOException | WriterException e) {
                            logger.error("生成二维码出错", e);
                        }
                    });
                    break;
                case EMAIL:
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {

                            SmtpProfile sp = cacheService.getSmtp();
                            if (sp == null) {
                                logger.error("SMTP信息未设置");
                                return;
                            }
                            try {
                                email(sp.getHost(), sp.getPort(), sp.getUsername(), sp.getPassword(), sp.isSsl(), sp.getBcc(),
                                        message.getString("to"), message.getString("title"), message.getString("body"), message.getBoolean("html"),
                                        (Map<String, String>) message.getObject("attachs"));
                            } catch (JMSException ex) {
                                logger.error("消息格式有误", ex);
                            }
                        }

                        private void email(String host,
                                           int port,
                                           String username,
                                           String password,
                                           boolean ssl,
                                           String bcc,
                                           String to, String title, String body, boolean html,
                                           Map<String, String> attachs) {
                            logger.debug("准备给[{}]发送邮件", to);
                            JavaMailSenderImpl sender = new JavaMailSenderImpl();
                            sender.setHost(host);
                            sender.setPort(port);
                            sender.setUsername(username);
                            sender.setPassword(password);
                            sender.setProtocol(ssl ? "smtps" : "smtp");
                            sender.setDefaultEncoding("UTF-8");
                            Properties props = new Properties();
                            props.put("mail.smtp.auth", true);
                            props.put("mail.smtp.timeout", 25000);
                            props.put("mail.smtp.quitwait", false);
                            if (ssl) {
                                props.put("mail.smtps.auth", true);
                                props.put("mail.smtp.starttls.enable", ssl);
                            }
                            sender.setJavaMailProperties(props);

                            try {
                                MimeMessage message = sender.createMimeMessage();
                                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                                helper.setSubject(title);
                                helper.setTo(to);
                                helper.setFrom(username);
                                if (bcc != null) {
                                    helper.setBcc(bcc);
                                }
                                helper.setText(body, html);
                                for (String file : attachs.keySet()) {
                                    helper.addInline(attachs.get(file), new FileSystemResource(file));
                                }
                                sender.send(message);
                                logger.debug("发送邮件成功[{},{}]", to, title);
                            } catch (Exception e) {
                                logger.error("发送邮件失败", e);
                            }
                        }
                    });
                    break;
                case VISIT:
                    switch (message.getString("type")) {
                        case "article":
                            contentService.setArticleVisits(message.getLong("id"));
                            break;
                        case "tag":
                            contentService.setTagVisits(message.getLong("id"));
                            break;
                        default:
                            logger.error("未知的访问类型");
                    }
                    break;
                default:
                    logger.error("未知的任务类型[{}]", type);
            }

        } catch (JMSException e) {
            logger.error("任务消息结构错误", e);

        }

    }

    private void text(TextMessage message) {
        try {
            logger.debug("收到文本消息[{}]", message.getText());
        } catch (JMSException e) {
            logger.error("处理文本消息出错", e);
        }
    }


    @Resource
    private DBHelper dbHelper;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TaskExecutor taskExecutor;
    @Resource
    private CacheService cacheService;
    @Resource
    private AccountService accountService;
    @Resource
    private ContentService contentService;
    @Value("${app.store}")
    private String appStoreDir;
    private final static Logger logger = LoggerFactory.getLogger(TaskListener.class);

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }
}
