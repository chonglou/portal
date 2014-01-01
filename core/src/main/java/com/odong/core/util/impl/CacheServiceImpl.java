package com.odong.core.util.impl;

import com.odong.core.cache.CacheHelper;
import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.model.SmtpProfile;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.web.Link;
import com.odong.web.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-31下午2:34.
 */
@Component("core.cacheService")
public class CacheServiceImpl implements CacheService {
    @Override
    public Page getPage() {
        return cacheHelper.get("page/model", Page.class, null, () -> {
            Page page = new Page();
            page.setAuthor(siteService.get("site.author", String.class));
            page.setTitle(siteService.get("site.title", String.class));
            page.setKeywords(siteService.get("site.keywords", String.class));
            page.setDescription(siteService.get("site.description", String.class));
            page.setCopyright(siteService.get("site.copyright", String.class));
            page.sethAd(siteService.get("site.hAd", String.class));
            page.setvAd(siteService.get("site.vAd", String.class));
            page.setCalendar("/archive");
            page.setGoogleAuth(siteService.get("site.auth.google", GoogleAuthProfile.class, true));
            page.setQqAuth(siteService.get("site.auth.qq", QqAuthProfile.class, true));

            page.getTopLinks().add(new Link("站点首页", "/main"));
            page.getTopLinks().add(new Link("用户中心", "/personal/self"));
            page.getTopLinks().add(new Link("网站地图", "/site/map"));
            page.getTopLinks().add(new Link("知识库", "/wiki"));
            page.getTopLinks().add(new Link("关于我们", "/aboutMe"));

            page.setDebug(appDebug);
            return page;
        });
    }

    @Override
    public void popPage() {
        cacheHelper.delete("page/model");
    }

    @Override
    public SmtpProfile getSmtp() {
        return cacheHelper.get("site/smtp", SmtpProfile.class, null, () -> siteService.get("site.smtp", SmtpProfile.class, true));
    }

    @Override
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
