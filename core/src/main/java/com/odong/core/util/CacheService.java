package com.odong.core.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.cache.RedisHelper;
import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.model.SmtpProfile;
import com.odong.core.service.SiteService;
import com.odong.web.model.Card;
import com.odong.web.model.Link;
import com.odong.web.model.Page;
import com.odong.web.model.RssItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by flamen on 13-12-31下午2:34.
 */
@Component("core.cacheService")
public class CacheService {

    public QqAuthProfile getQqAuthProfile(){
        return cacheHelper.get("oauth/google", QqAuthProfile.class, null, ()->siteService.get("site.oauth.qq", QqAuthProfile.class, true));
    }
    public void popQqAuthProfile(){
        cacheHelper.delete("oauth/qq");
    }
    public GoogleAuthProfile getGoogleAuthProfile(){
        return cacheHelper.get("oauth/google", GoogleAuthProfile.class, null, ()->siteService.get("site.oauth.google", GoogleAuthProfile.class, true));
    }
    public void popGoogleAuthProfile(){
        cacheHelper.delete("oauth/google");
    }

    public String getAboutMe(){
        return cacheHelper.get("site/aboutMe", String.class, null, ()->siteService.get("site.aboutMe", String.class));
    }
    public void popAboutMe(){
        cacheHelper.delete("site/aboutMe");
    }

    public Page getPage() {
        return cacheHelper.get("page/model", Page.class, null, () -> {
            //FIXME 不应在这里出现
            Page page = new Page();
            page.setAuthor(siteService.get("site.author", String.class));
            page.setTitle(siteService.get("site.title", String.class));
            page.setKeywords(siteService.get("site.keywords", String.class));
            page.setDescription(siteService.get("site.description", String.class));
            page.setCopyright(siteService.get("site.copyright", String.class));
            page.sethAd(siteService.get("site.hAd", String.class));
            page.setvAd(siteService.get("site.vAd", String.class));
            page.setCalendar("/archive");

            //FIXME
            page.getTopLinks().add(new Link("站点首页", "/main"));
            page.getTopLinks().add(new Link("用户中心", "/personal/self"));
            page.getTopLinks().add(new Link("网站地图", "/site/map"));
            page.getTopLinks().add(new Link("知识库", "/wiki"));
            page.getTopLinks().add(new Link("关于我们", "/aboutMe"));

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
