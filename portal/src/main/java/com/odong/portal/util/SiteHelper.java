package com.odong.portal.util;

import com.odong.portal.entity.User;
import com.odong.portal.model.KaptchaProfile;
import com.odong.portal.model.ReCaptchaProfile;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.RbacService;
import com.odong.portal.service.SiteService;
import httl.spi.resolvers.GlobalResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:41
 */
@Component("siteHelper")
public class SiteHelper {
    @PostConstruct
    void init() {
        siteService.set("site.startup", new Date());
        if (siteService.getObject("site.init", Date.class) == null) {

            siteService.set("site.init", new Date());
            siteService.set("site.version", "v20130522");
            siteService.set("site.title", "门户网站系统");
            siteService.set("site.description", "站点说明信息");
            siteService.set("site.keywords", "站点关键字");
            siteService.set("site.domain", "www.0-dong.com");
            siteService.set("site.copyright", "&copy;2013");
            siteService.set("site.allowRegister", true);
            siteService.set("site.allowLogin", true);
            siteService.set("site.allowAnonym", true);
            siteService.set("site.aboutMe", "关于我们");
            siteService.set("site.regProtocol", "注册协议");
            siteService.set("site.author", "zhengjitang@gmail.com");

            //KAPTCHA
            KaptchaProfile kaptcha = new KaptchaProfile();
            kaptcha.setLength(4);
            kaptcha.setChars("0123456789");
            kaptcha.setHeight(56);
            kaptcha.setWidth(100);
            siteService.set("site.kaptcha", kaptcha);
            //RECAPTCHA
            siteService.set("site.reCaptcha", new ReCaptchaProfile());
            siteService.set("site.captcha", "kaptcha");

            //USER
            accountService.addUser(manager, "管理员", "123456");
            User admin = accountService.getUser(manager);
            rbacService.bindAdmin(admin.getId(), true);

            //CMS
            siteService.set("site.hotTagCount", 20);
            siteService.set("site.hotArticleCount", 10);
            siteService.set("site.latestCommentCount", 10);
            siteService.set("site.archiveCount", 6);
            siteService.set("site.articlePageSize", 50);

            contentService.addTag("默认", true);
            contentService.addTag("置顶", true);

            //广告
            siteService.set("site.advert.bottom", "<script type=\"text/javascript\"><!--\n" +
                    "google_ad_client = \"ca-pub-5028403610236620\";\n" +
                    "/* 全局横幅 */\n" +
                    "google_ad_slot = \"8184321813\";\n" +
                    "google_ad_width = 728;\n" +
                    "google_ad_height = 90;\n" +
                    "//-->\n" +
                    "</script>\n" +
                    "<script type=\"text/javascript\"\n" +
                    "src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">\n" +
                    "</script>");
            siteService.set("site.advert.left", "<script type=\"text/javascript\"><!--\n" +
                    "google_ad_client = \"ca-pub-5028403610236620\";\n" +
                    "/* 底部商铺（discuz） */\n" +
                    "google_ad_slot = \"6080910921\";\n" +
                    "google_ad_width = 300;\n" +
                    "google_ad_height = 250;\n" +
                    "//-->\n" +
                    "</script>\n" +
                    "<script type=\"text/javascript\"\n" +
                    "src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">\n" +
                    "</script>");

        }

        GlobalResolver.put("gl_debug", appDebug);

        File base = new File(appStoreDir);
        if (base.exists()) {
            if (!base.isDirectory() || !base.canWrite()) {
                throw new RuntimeException("数据存储目录[" + base.getAbsolutePath() + "]不可用");
            }
        }
        if (!base.exists()) {
            logger.info("数据存储目录[{}]不存在,创建之!", appStoreDir);
            for (String s : new String[]{"backup", "seo", "attach"}) {
                String dir = appStoreDir + "/" + s;
                File f = new File(dir);
                if (f.mkdirs()) {
                    logger.info("创建数据目录[{}]成功", dir);
                } else {
                    throw new IllegalArgumentException("数据存储目录[" + f.getAbsolutePath() + "]创建失败");
                }
            }
        }
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }

    @Resource
    private RbacService rbacService;
    @Resource
    private AccountService accountService;
    @Resource
    private ContentService contentService;
    @Resource
    private SiteService siteService;
    @Value("${app.store}")
    private String appStoreDir;
    @Value("${app.debug}")
    private boolean appDebug;
    @Value("${app.manager}")
    private String manager;
    private final static Logger logger = LoggerFactory.getLogger(SiteHelper.class);

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }


    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setAppDebug(boolean appDebug) {
        this.appDebug = appDebug;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
