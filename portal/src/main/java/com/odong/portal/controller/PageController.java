package com.odong.portal.controller;

import com.odong.portal.model.SessionItem;
import com.odong.portal.service.*;
import com.odong.portal.util.CacheService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.ResponseItem;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-21
 * Time: 下午7:07
 */
public abstract class PageController {

    protected void checkLogin(ResponseItem ri, SessionItem si) {
        if (si.getSsUserId() == null) {
            ri.setOk(false);
            ri.addData("需要登陆");
        }
    }


    protected List<NavBar> getNavBars() {
        List<NavBar> navBars = new ArrayList<>();
        navBars.add(cacheService.getLatestArticleNavBar());
        navBars.add(cacheService.getLeastCommentNavBar());
        navBars.add(cacheService.getRecentArchiveNavBar());
        return navBars;

    }

    protected void fillSiteInfo(Map<String, Object> map) {
        map.put("gl_site", cacheService.getSiteInfo());

    }


    @Resource
    protected CacheService cacheService;
    @Resource
    protected SiteService siteService;
    @Resource
    protected ContentService contentService;
    @Resource
    protected FormHelper formHelper;
    @Resource
    protected LogService logService;
    @Resource
    protected AccountService accountService;
    @Value("${app.manager}")
    protected String manager;

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
