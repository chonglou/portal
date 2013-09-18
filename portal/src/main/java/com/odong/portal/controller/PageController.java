package com.odong.portal.controller;

import com.odong.portal.entity.FriendLink;
import com.odong.portal.entity.Tag;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.Page;
import com.odong.portal.web.ResponseItem;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
        navBars.add(cacheHelper.get("navBar/hotArticle", NavBar.class, null, () -> {
            NavBar nb = new NavBar("热门文章");
            nb.setType(NavBar.Type.LIST);
            contentService.hotArticle(siteService.getInteger("site.hotArticleCount")).forEach((a) -> nb.add(a.getTitle(), "/article/" + a.getId()));

            return nb;
        }));
        navBars.add(cacheHelper.get("navBar/leastComment", NavBar.class, 60 * 60, () -> {
            NavBar nb = new NavBar("最新评论");
            nb.setType(NavBar.Type.LIST);
            contentService.latestComment(siteService.getInteger("site.latestCommentCount")).forEach((c) -> nb.add(c.getContent(), "/comment/" + c.getId()));
            return nb;
        }));
        navBars.add(cacheHelper.get("navBar/recentArchive", NavBar.class, null, () -> {
            NavBar nb = new NavBar("最近归档");
            nb.setType(NavBar.Type.LIST);
            DateTime init = new DateTime(siteService.getDate("site.init"));
            for (int i = 0; i < siteService.getInteger("site.archiveCount"); i++) {
                DateTime dt = new DateTime().plusMonths(0 - i);
                if (dt.compareTo(init) >= 0) {
                    addArchive2NavBar(nb, dt);
                } else {
                    break;
                }
            }
            return nb;
        }));
        return navBars;

    }

    protected void addArchive2NavBar(NavBar nb, DateTime dt) {
        nb.add(dt.toString("yyyy年MM月"), "/archive/" + dt.toString("yyyy-MM"));
    }

    protected void fillSiteInfo(Map<String, Object> map) {

        map.put("gl_site", cacheHelper.get("site/info", HashMap.class, null,
                () -> {
                    HashMap<String, Object> site = new HashMap<>();
                    for (String s : new String[]{"title", "description", "copyright", "keywords", "author", "articlePageSize"}) {
                        site.put(s, siteService.getString("site." + s));
                    }

                    Map<String, String> topNavs = new HashMap<>();
                    topNavs.put("main", "站点首页");
                    topNavs.put("personal/self", "用户中心");
                    topNavs.put("sitemap", "网站地图");
                    topNavs.put("aboutMe", "关于我们");
                    site.put("topNavs", topNavs);

                    List<Page> tagCloud = new ArrayList<>();
                    for (Tag tag : contentService.hotTag(siteService.getInteger("site.hotTagCount"))) {
                        tagCloud.add(new Page(tag.getName(), "/tag/" + tag.getId()));
                    }
                    for (FriendLink fl : siteService.listFriendLink()) {
                        tagCloud.add(new Page(fl.getName(), fl.getUrl()));
                    }
                    site.put("tagCloud", tagCloud);
                    site.put("advertLeft", siteService.getString("site.advert.left"));
                    site.put("advertBottom", siteService.getString("site.advert.bottom"));
                    return site;
                }
        ));

    }

    @Resource
    protected CacheHelper cacheHelper;
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


    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
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
