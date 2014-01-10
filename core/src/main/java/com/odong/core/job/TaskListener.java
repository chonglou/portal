package com.odong.core.job;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.odong.core.model.QrCodeProfile;
import com.odong.core.model.SmtpProfile;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.service.SiteService;
import com.odong.core.store.DbUtil;
import com.odong.core.util.CacheService;
import com.odong.web.model.RssItem;
import com.odong.web.model.SitemapItem;
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by flamen on 13-12-30下午8:10.
 */
@Component("core.job.listener")
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
            String type = message.getStringProperty("type");
            String module = message.getStringProperty("module");
            String itemId = message.getJMSCorrelationID();
            switch (type) {
                case "email":
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            SmtpProfile sp = cacheService.getSmtp();

                            if (sp == null) {
                                logger.error("SMTP信息未设置");
                                return;
                            }
                            try {
                                email(sp.getHost(), sp.getPort(), sp.getUsername(), sp.getPassword(),
                                        sp.isSsl(), sp.getBcc(),
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
                case "gc":
                    System.gc();
                    break;
                case "rss":
                    taskExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            try (Writer writer = new FileWriter(appStoreDir + "/seo/rss.xml")) {
                                String domain = "http://" + siteService.get("site.domain", String.class);
                                SyndFeed feed = new SyndFeedImpl();
                                feed.setFeedType("rss_2.0");
                                feed.setTitle(siteService.get("site.title", String.class));
                                feed.setLink(domain);
                                feed.setDescription(siteService.get("site.description", String.class));
                                List<SyndEntry> entries = new ArrayList<>();

                                entries.add(createRssSyndEntry(domain + "/aboutMe", "关于我们", siteService.get("site.aboutMe", String.class), siteService.get("site.init", Date.class)));
                                pluginUtil.foreach((Plugin plugin) -> {
                                    for (RssItem ri : plugin.rss()) {
                                        entries.add(createRssSyndEntry(ri.getUrl(), ri.getTitle(), ri.getSummary(), ri.getPublish()));
                                    }
                                });

                                feed.setEntries(entries);
                                SyndFeedOutput output = new SyndFeedOutput();
                                output.output(feed, writer);
                            } catch (IOException | FeedException e) {
                                logger.error("生成RSS出错", e);
                            }
                        }

                        private SyndEntry createRssSyndEntry(String url, String title, String body, Date publishedDate) {
                            SyndEntry entry = new SyndEntryImpl();
                            entry.setTitle(title);
                            entry.setLink(url);
                            entry.setPublishedDate(publishedDate);
                            SyndContent aboutDesc = new SyndContentImpl();
                            aboutDesc.setType("text/html");
                            aboutDesc.setValue(body);
                            entry.setDescription(aboutDesc);
                            return entry;
                        }
                    });
                    break;
                case "qrcode":
                    taskExecutor.execute(() -> {
                        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
                        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                        QrCodeProfile qcp = siteService.get("site.qrCode", QrCodeProfile.class);
                        if (qcp == null) {
                            qcp = new QrCodeProfile("https://code.google.com/p/latrop/", 300, 300);
                        }
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
                case "sitemap":
                    taskExecutor.execute(() -> {
                        try {
                            String domain = "http://" + siteService.get("site.domain", String.class);
                            WebSitemapGenerator wsg = WebSitemapGenerator.builder(domain, new File(appStoreDir + "/seo/")).gzip(true).build();
                            wsg.addUrl(new WebSitemapUrl.Options(domain + "/aboutMe").lastMod(siteService.get("site.init", Date.class)).priority(0.9).changeFreq(ChangeFreq.YEARLY).build());
                            wsg.addUrl(new WebSitemapUrl.Options(domain + "/sitemap").lastMod(new Date()).priority(0.9).changeFreq(ChangeFreq.DAILY).build());

                            pluginUtil.foreach((Plugin plugin) -> {
                                for (SitemapItem si : plugin.sitemap()) {
                                    try {
                                        wsg.addUrl(new WebSitemapUrl.Options(si.getUrl()).lastMod(si.getPublish()).priority(si.getPriority()).changeFreq(ChangeFreq.valueOf(si.getChangeFreq())).build());
                                    } catch (MalformedURLException e) {
                                        logger.error("创建sitemap.xml出错", e);
                                    }
                                }
                            });

                            DateTime now = new DateTime();
                            for (DateTime dt = new DateTime(siteService.get("site.init", Date.class)); dt.compareTo(now) <= 0; dt = dt.plusMonths(1)) {
                                wsg.addUrl(new WebSitemapUrl.Options(domain + "/archive/" + dt.toString("yyyy/MM")).lastMod(dt.toDate()).priority(0.2).changeFreq(ChangeFreq.MONTHLY).build());
                            }


                            wsg.write();
                        } catch (IOException e) {
                            logger.error("创建sitemap.xml出错", e);
                        }
                    });
                    break;
                case "backup":
                    taskExecutor.execute(() -> {
                        dbUtil.backup();
                        siteService.set("site.lastBackup", new Date());
                    });
                    break;
                default:
                    if (pluginUtil.isEnable(module)) {
                        logger.debug("任务转送至模块[{}]", module);
                        pluginUtil.getPlugin(module).onMessage(message);
                    } else {
                        logger.debug("模块[{}]未启用", module);
                    }
                    break;
            }


        } catch (JMSException e) {
            logger.error("任务消息结构错误", e);

        }

    }

    @PostConstruct
    void init() {
        for (String s : new String[]{"seo", "backup", "attach"}) {
            File file = new File(appStoreDir + "/" + s);
            if (!file.exists()) {
                logger.info("目录[{}]不存在", file.getAbsolutePath());
                if (!file.mkdirs()) {
                    logger.error("创建数据目录[{}]失败", file.getAbsolutePath());
                }
            }
        }
    }

    private void text(TextMessage message) {
        try {
            logger.debug("收到文本消息[{}]", message.getText());
        } catch (JMSException e) {
            logger.error("处理文本消息出错", e);
        }
    }

    @Resource(name = "core.taskExecutor")
    private TaskExecutor taskExecutor;
    @Resource
    private CacheService cacheService;
    @Resource
    private DbUtil dbUtil;
    @Resource
    private SiteService siteService;
    @Resource
    private PluginUtil pluginUtil;
    @Value("${app.store}")
    private String appStoreDir;
    private final static Logger logger = LoggerFactory.getLogger(TaskListener.class);


    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}
