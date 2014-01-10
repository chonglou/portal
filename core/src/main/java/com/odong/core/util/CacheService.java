package com.odong.core.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.model.ReCaptchaProfile;
import com.odong.core.model.SmtpProfile;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.service.SiteService;
import com.odong.web.model.Link;
import com.odong.web.model.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-31下午2:34.
 */
@Component("core.cacheService")
public class CacheService {
    public ReCaptchaProfile getReCaptcha() {
        return cacheHelper.get("site/reCaptcha", ReCaptchaProfile.class, null, () -> siteService.get("site.reCaptcha", ReCaptchaProfile.class, true));
    }

    public void popReCaptcha() {
        cacheHelper.delete("site/reCaptcha");
    }

    public String getCaptcha() {
        return cacheHelper.get("site/captcha", String.class, null, () -> siteService.get("site.captcha", String.class));
    }

    public void popCaptcha() {
        cacheHelper.delete("site/captcha");
    }

    public String getDefaultPlugin() {
        return cacheHelper.get("site/plugin/default", String.class, null, () -> siteService.get("site.plugin.default", String.class));
    }

    public void popDefaultPlugin() {
        cacheHelper.delete("site/plugin/default");
    }

    public String getRegProtocol() {
        return cacheHelper.get("site/regProtocol", String.class, null, () -> siteService.get("site.regProtocol", String.class));
    }

    public void popRegProtocol() {
        cacheHelper.delete("site/regProtocol");
    }

    public String getSiteDomain() {
        return cacheHelper.get("site/domain", String.class, null, () -> siteService.get("site.domain", String.class));
    }

    public void popSiteDomain() {
        cacheHelper.delete("site/domain");
    }

    public String getSiteTitle() {
        return cacheHelper.get("site/title", String.class, null, () -> siteService.get("site.title", String.class));
    }

    public void popSiteTitle() {
        cacheHelper.delete("site/title");
    }

    public int getSearchSpace() {
        return cacheHelper.get("site/searchSpace", Integer.class, null, () -> siteService.get("site.searchSpace", Integer.class));
    }

    public void popSearchSpace() {
        cacheHelper.delete("site/searchSpace");
    }
    public int getLinkValid() {
        return cacheHelper.get("site/linkValid", Integer.class, null, () -> siteService.get("site.linkValid", Integer.class));
    }

    public void popLinkValid() {
        cacheHelper.delete("site/linkValid");
    }


    public QqAuthProfile getQqAuthProfile() {
        return cacheHelper.get("oauth/qq", QqAuthProfile.class, null, () -> siteService.get("site.oauth.qq", QqAuthProfile.class, true));
    }

    public void popQqAuthProfile() {
        cacheHelper.delete("oauth/qq");
    }

    public GoogleAuthProfile getGoogleAuthProfile() {
        return cacheHelper.get("oauth/google", GoogleAuthProfile.class, null, () -> siteService.get("site.oauth.google", GoogleAuthProfile.class, true));
    }

    public void popGoogleAuthProfile() {
        cacheHelper.delete("oauth/google");
    }

    public String getAboutMe() {
        return cacheHelper.get("site/aboutMe", String.class, null, () -> siteService.get("site.aboutMe", String.class));
    }

    public void popAboutMe() {
        cacheHelper.delete("site/aboutMe");
    }

    public Page getPage() {
        return cacheHelper.get("page/model", Page.class, null, () -> {
            Page page = new Page();

            page.setAuthor(siteService.get("site.author", String.class));
            page.setKeywords(siteService.get("site.keywords", String.class));
            page.setDescription(siteService.get("site.description", String.class));
            page.setCopyright(siteService.get("site.copyright", String.class));
            page.setAdLeft(siteService.get("site.ad.left", String.class));
            page.setAdBottom(siteService.get("site.ad.bottom", String.class));

            pluginUtil.foreach((Plugin plugin) -> {
                page.getTopLinks().addAll(plugin.getTopLinks());
                page.getSideBars().addAll(plugin.getSideBars());
                page.getTagCloud().addAll(plugin.getTagCloud());
            });

            page.getTopLinks().add(new Link("用户中心", "/personal/self"));
            page.getTopLinks().add(new Link("网站地图", "/sitemap"));
            page.getTopLinks().add(new Link("关于我们", "/aboutMe"));
            page.getTagCloud().add(new Link("LATROP内容管理系统", "https://code.google.com/p/latrop/"));

            page.setDebug(appDebug);
            return page;
        });
    }

    public void popPage() {
        cacheHelper.delete("page/model");
    }

    public SmtpProfile getSmtp() {
        return cacheHelper.get("site/smtp", SmtpProfile.class, null, () -> siteService.get("site.smtp", SmtpProfile.class, true));
    }

    public void popSmtp() {
        cacheHelper.delete("site/smtp");
    }


    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private CacheHelper cacheHelper;
    @Value("${app.debug}")
    private boolean appDebug;
    @Resource
    private PluginUtil pluginUtil;

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setAppDebug(boolean appDebug) {
        this.appDebug = appDebug;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
