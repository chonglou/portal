package com.odong.portal.util;

import com.odong.portal.entity.Task;
import com.odong.portal.entity.User;
import com.odong.portal.model.Contact;
import com.odong.portal.model.profile.KaptchaProfile;
import com.odong.portal.model.profile.QrCodeProfile;
import com.odong.portal.model.profile.ReCaptchaProfile;
import com.odong.portal.model.task.ClockRequest;
import com.odong.portal.service.*;
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
            siteService.set("site.version", "v20131006");
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
            siteService.set("site.google.valid", stringHelper.random(8));
            siteService.set("site.qrCode", new QrCodeProfile("", 300, 300));

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
            long admin = accountService.addUser(manager, "系统管理员", "123456");
            accountService.setUserState(admin, User.State.ENABLE);
            rbacService.bindAdmin(admin, true);

            //CMS
            siteService.set("site.hotTagCount", 20);
            siteService.set("site.latestArticleCount", 10);
            siteService.set("site.latestCommentCount", 10);
            siteService.set("site.archiveCount", 6);
            siteService.set("site.articlePageSize", 50);

            siteService.set("site.defTag", contentService.addTag("默认", true));
            siteService.set("site.topTag", contentService.addTag("置顶", true));

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
            //GOOGLE SEARCH
            siteService.set("site.google.search", "<style type=\"text/css\">\n" +
                    "@import url(http://www.google.com/cse/api/branding.css);\n" +
                    "</style>\n" +
                    "<div class=\"cse-branding-right\" style=\"background-color:#FFFFFF;color:#000000\">\n" +
                    "  <div class=\"cse-branding-form\">\n" +
                    "    <form action=\"http://www.google.com.hk\" id=\"cse-search-box\" target=\"_blank\">\n" +
                    "      <div>\n" +
                    "        <input type=\"hidden\" name=\"cx\" value=\"partner-pub-5028403610236620:4707578439\" />\n" +
                    "        <input type=\"hidden\" name=\"ie\" value=\"UTF-8\" />\n" +
                    "        <input type=\"text\" name=\"q\" size=\"55\" />\n" +
                    "        <input type=\"submit\" name=\"sa\" value=\"搜索\" />\n" +
                    "      </div>\n" +
                    "    </form>\n" +
                    "    <script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n" +
                    "    <script type=\"text/javascript\">google.load(\"elements\", \"1\", {packages: \"transliteration\"});</script>\n" +
                    "    <script type=\"text/javascript\" src=\"http://www.google.com/cse/t13n?form=cse-search-box&t13n_langs=en\"></script>\n" +
                    "  </div>\n" +
                    "  <div class=\"cse-branding-logo\">\n" +
                    "    <img src=\"http://www.google.com/images/poweredby_transparent/poweredby_FFFFFF.gif\" alt=\"Google\" />\n" +
                    "  </div>\n" +
                    "  <div class=\"cse-branding-text\">\n" +
                    "    自定义搜索\n" +
                    "  </div>\n" +
                    "</div>\n");

            //社交网站分享代码
            siteService.set("site.share.qq", "   <script type=\"text/javascript\">\n" +
                    "(function(){\n" +
                    "var p = {\n" +
                    "url:location.href, /*获取URL，可加上来自分享到QQ标识，方便统计*/\n" +
                    "desc:'', /*分享理由(风格应模拟用户对话),支持多分享语随机展现（使用|分隔）*/\n" +
                    "title:'', /*分享标题(可选)*/\n" +
                    "summary:'', /*分享摘要(可选)*/\n" +
                    "pics:'', /*分享图片(可选)*/\n" +
                    "flash: '', /*视频地址(可选)*/\n" +
                    "site:'', /*分享来源(可选) 如：QQ分享*/\n" +
                    "style:'101',\n" +
                    "width:96,\n" +
                    "height:24\n" +
                    "};\n" +
                    "var s = [];\n" +
                    "for(var i in p){\n" +
                    "s.push(i + '=' + encodeURIComponent(p[i]||''));\n" +
                    "}\n" +
                    "document.write(['<a class=\"qcShareQQDiv\" href=\"http://connect.qq.com/widget/shareqq/index.html?',s.join('&'),'\" target=\"_blank\">分享到QQ</a>'].join(''));\n" +
                    "})();\n" +
                    "</script>\n" +
                    "<script src=\"http://connect.qq.com/widget/loader/loader.js\" widget=\"shareqq\" charset=\"utf-8\"></script>\n");
            siteService.set("site.share.qZone", " <script type=\"text/javascript\">\n" +
                    "(function(){\n" +
                    "var p = {\n" +
                    "url:location.href,\n" +
                    "showcount:'1',/*是否显示分享总数,显示：'1'，不显示：'0' */\n" +
                    "desc:'',/*默认分享理由(可选)*/\n" +
                    "summary:'',/*分享摘要(可选)*/\n" +
                    "title:'',/*分享标题(可选)*/\n" +
                    "site:'',/*分享来源 如：腾讯网(可选)*/\n" +
                    "pics:'', /*分享图片的路径(可选)*/\n" +
                    "style:'101',\n" +
                    "width:199,\n" +
                    "height:30\n" +
                    "};\n" +
                    "var s = [];\n" +
                    "for(var i in p){\n" +
                    "s.push(i + '=' + encodeURIComponent(p[i]||''));\n" +
                    "}\n" +
                    "document.write(['<a version=\"1.0\" class=\"qzOpenerDiv\" href=\"http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?',s.join('&'),'\" target=\"_blank\">分享</a>'].join(''));\n" +
                    "})();\n" +
                    "</script>\n" +
                    "<script src=\"http://qzonestyle.gtimg.cn/qzone/app/qzlike/qzopensl.js#jsdate=20111201\" charset=\"utf-8\"></script>\n");

            addClockTask(Task.Type.SITE_MAP, 3);
            addClockTask(Task.Type.RSS, 3);
            addClockTask(Task.Type.GC, 2);
            addClockTask(Task.Type.BACKUP, 4);
        }


        logger.info("DEBUG模式[{}]", appDebug);
        GlobalResolver.put("gl_debug", appDebug);

        logger.info("用户数据目录{}", appStoreDir);
        for (String s : new String[]{"backup", "seo", "attach"}) {
            String dir = appStoreDir + "/" + s;
            File f = new File(dir);
            if (f.exists()) {
                if (!f.isDirectory() || !f.canWrite()) {
                    throw new RuntimeException("数据存储目录[" + f.getAbsolutePath() + "]不可用");
                }
            } else {
                logger.info("数据存储目录[{}]不存在,创建之!", appStoreDir);
                if (f.mkdirs()) {
                    logger.info("创建数据目录[{}]成功", dir);
                } else {
                    throw new IllegalArgumentException("数据存储目录[" + f.getAbsolutePath() + "]创建失败");
                }
            }
        }

        checkUpdate();
    }

    private void checkUpdate() {

        if ("v20130522".equals(siteService.getString("site.version"))) {
            //升级默认配置
            siteService.set("site.latestArticleCount", 10);
            siteService.set("site.google.search", "<style type=\"text/css\">\n" +
                    "@import url(http://www.google.com/cse/api/branding.css);\n" +
                    "</style>\n" +
                    "<div class=\"cse-branding-right\" style=\"background-color:#FFFFFF;color:#000000\">\n" +
                    "  <div class=\"cse-branding-form\">\n" +
                    "    <form action=\"http://www.google.com.hk\" id=\"cse-search-box\" target=\"_blank\">\n" +
                    "      <div>\n" +
                    "        <input type=\"hidden\" name=\"cx\" value=\"partner-pub-5028403610236620:4707578439\" />\n" +
                    "        <input type=\"hidden\" name=\"ie\" value=\"UTF-8\" />\n" +
                    "        <input type=\"text\" name=\"q\" size=\"55\" />\n" +
                    "        <input type=\"submit\" name=\"sa\" value=\"搜索\" />\n" +
                    "      </div>\n" +
                    "    </form>\n" +
                    "    <script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n" +
                    "    <script type=\"text/javascript\">google.load(\"elements\", \"1\", {packages: \"transliteration\"});</script>\n" +
                    "    <script type=\"text/javascript\" src=\"http://www.google.com/cse/t13n?form=cse-search-box&t13n_langs=en\"></script>\n" +
                    "  </div>\n" +
                    "  <div class=\"cse-branding-logo\">\n" +
                    "    <img src=\"http://www.google.com/images/poweredby_transparent/poweredby_FFFFFF.gif\" alt=\"Google\" />\n" +
                    "  </div>\n" +
                    "  <div class=\"cse-branding-text\">\n" +
                    "    自定义搜索\n" +
                    "  </div>\n" +
                    "</div>\n");
            siteService.set("site.version", "v20131006");
        }

    }

    private void addClockTask(Task.Type type, int clock) {
        siteService.set("task." + type, taskService.addTask(
                type,
                new ClockRequest(clock),
                new Date(),
                timeHelper.max(),
                timeHelper.nextDay(clock)
        ));
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
    @Resource
    private TaskService taskService;
    @Resource
    private TimeHelper timeHelper;
    @Resource
    private StringHelper stringHelper;
    @Value("${app.store}")
    private String appStoreDir;
    @Value("${app.debug}")
    private boolean appDebug;
    @Value("${app.manager}")
    private String manager;
    private final static Logger logger = LoggerFactory.getLogger(SiteHelper.class);

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
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
