package com.odong.portal.controller;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Comment;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.ResponseItem;
import org.joda.time.DateTime;

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

    protected void checkLogin(ResponseItem ri, SessionItem si){
        if(si.getSsUserId() == null){
            ri.setOk(false);
            ri.addData("需要登陆");
        }
    }

    protected Map<Long,User> getUserMap(List<Article> articles){
        Map<Long,User> users = new HashMap<>();
        for(Article a : articles){
            if(users.get(a.getAuthor()) == null){
                users.put(a.getAuthor(), accountService.getUser(a.getAuthor()));
            }
        }
        return users;
    }

    protected List<NavBar> getNavBars() {

        List<NavBar> navBars = new ArrayList<>();

        NavBar nbArticle = new NavBar("热门文章");
        for (Article a : contentService.hotArticle(siteService.getInteger("site.hotArticleCount"))) {
            nbArticle.add(a.getTitle(), "/article/" + a.getId());
        }
        navBars.add(nbArticle);

        NavBar nbComment = new NavBar("最新评论");
        for (Comment c : contentService.latestComment(siteService.getInteger("site.latestCommentCount"))) {
            nbComment.add(c.getContent(), "/comment/" + c.getId());
        }
        navBars.add(nbComment);

        NavBar nbArchive = new NavBar("最近归档");
        DateTime init = new DateTime(siteService.getDate("site.init"));
        for (int i = 0; i < siteService.getInteger("site.archiveCount"); i++) {
            DateTime dt = new DateTime().plusMonths(0 - i);
            if (dt.compareTo(init) >= 0) {
                addArchive2NavBar(nbArchive, dt);
            } else {
                break;
            }
        }
        navBars.add(nbArchive);
        return navBars;
    }

    protected void addArchive2NavBar(NavBar nb, DateTime dt) {
        nb.add(dt.toString("yyyy年MM月"), "/archive/" + dt.toString("yyyy-MM"));
    }

    protected void fillSiteInfo(Map<String, Object> map) {
        //TODO 需要Cache缓存
        Map<String, Object> site = new HashMap<>();
        for (String s : new String[]{"title", "description", "copyright", "keywords", "author", "articlePageSize"}) {
            site.put(s, siteService.getString("site." + s));
        }


        Map<String, String> topNavs = new HashMap<>();
        topNavs.put("main", "站点首页");
        topNavs.put("personal/self", "用户中心");
        topNavs.put("sitemap", "网站地图");
        topNavs.put("aboutMe", "关于我们");
        site.put("topNavs", topNavs);
        site.put("hotTags", contentService.hotTag(siteService.getInteger("site.hotTagCount")));
        map.put("gl_site", site);
    }


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
