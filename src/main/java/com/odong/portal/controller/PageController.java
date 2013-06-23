package com.odong.portal.controller;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Comment;
import com.odong.portal.entity.Tag;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import com.odong.portal.web.NavBar;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@Controller
@SessionAttributes(SessionItem.KEY)
public class PageController {


    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    void getIndex(HttpServletResponse response) throws IOException {
        response.sendRedirect("/personal/self");
    }

    @RequestMapping(value = "/personal/self", method = RequestMethod.GET)
    String getSelf(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        List<NavBar> navBars = new ArrayList<>();

        NavBar nbInfo = new NavBar("个人信息");
        nbInfo.add("基本信息", "/personal/info");
        nbInfo.add("日志管理", "/personal/log");
        nbInfo.setAjax(true);
        navBars.add(nbInfo);


        NavBar nbCms = new NavBar("内容管理");
        nbCms.add("新增文章", "/personal/article/add");
        nbCms.add("文章管理", "/personal/article");
        nbCms.add("评论管理", "/personal/comment");
        nbCms.setAjax(true);
        navBars.add(nbCms);

        if (si.isAdmin()) {
            NavBar nbSite = new NavBar("站点管理");
            nbSite.add("用户管理", "/admin/user");
            nbSite.add("标签管理", "/admin/tag");
            nbSite.add("版面管理", "/admin/board");
            nbSite.add("友情链接", "/admin/friend_link");
            nbSite.add("站点信息", "/admin/site");
            nbSite.add("日志管理", "/admin/log");
            nbSite.setAjax(true);
            navBars.add(nbSite);
        }
        map.put("navBars", navBars);
        fillSiteInfo(map);
        map.put("title", "用户中心");
        map.put("top_nav_key", "personal");
        return "main";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map) {

        List<NavBar> navBars = new ArrayList<>();
        NavBar nbTag = new NavBar("标签列表");
        int i = 0;
        for (Tag t : contentService.listTag()) {
            nbTag.add(t.getName(), "/tag/" + t.getId());
            i++;
        }
        nbTag.setTitle(nbTag.getTitle() + " [" + i + "]");
        navBars.add(nbTag);


        map.put("navBars", navBars);
        //TODO 分页
        map.put("articles", contentService.listArticle());
        fillSiteInfo(map);
        map.put("title", "网站地图");
        map.put("top_nav_key", "sitemap");
        return "sitemap";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map) {

        map.put("navBars", navBars());
        fillSiteInfo(map);
        map.put("title", "首页");
        map.put("top_nav_key", "main");
        //TODO 分页
        map.put("articles", contentService.latestArticle(siteService.getInteger("site.articlePageSize")));
        map.put("articlesSize", contentService.countArticle());
        return "main";
    }

    @RequestMapping(value = "/about_me", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", navBars());
        fillSiteInfo(map);
        map.put("title", "关于我们");
        map.put("top_nav_key", "about_me");
        return "about_me";
    }

    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
    void getComment(@PathVariable long commentId, HttpServletResponse response) throws IOException {
        response.sendRedirect("/article/" + contentService.getComment(commentId).getArticle() + "#" + commentId);
    }

    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    String getArticle(Map<String, Object> map, @PathVariable long articleId) {
        map.put("navBars", navBars());
        map.put("tags", contentService.listTagByArticle(articleId));
        map.put("comments", contentService.listCommentByArticle(articleId));
        Article a = contentService.getArticle(articleId);
        map.put("article", a);
        fillSiteInfo(map);
        map.put("title", a.getTitle());
        map.put("description", a.getSummary());

        return "falls";
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
    String getTag(Map<String, Object> map, @PathVariable long tagId) {
        map.put("navBars", navBars());
        //TODO 分页
        map.put("articles", contentService.listArticleByTag(tagId));
        fillSiteInfo(map);
        Tag t = contentService.getTag(tagId);
        map.put("title", "标签[" + t.getName() + "](" + t.getVisits() + ")");
        return "falls";
    }

    @RequestMapping(value = "/archive/{year}-{month}", method = RequestMethod.GET)
    String getArchive(Map<String, Object> map, @PathVariable int year, @PathVariable int month) {

        List<NavBar> navBars = new ArrayList<>();
        NavBar nbArchive = new NavBar("归档列表");
        DateTime now = new DateTime();

        for (DateTime init = new DateTime(siteService.getDate("site.init")); init.compareTo(now) <= 0; init = init.plusMonths(1)) {
            addArchive2NavBar(nbArchive, init);
        }
        navBars.add(nbArchive);
        map.put("navBars", navBars);
        //TODO 分页
        map.put("articles", contentService.listArticleByMonth(year, month));
        fillSiteInfo(map);
        map.put("title", new DateTime().withYear(year).withMonthOfYear(month).toString("yyyy年MM月"));
        return "falls";
    }

    private List<NavBar> navBars() {

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

    private void addArchive2NavBar(NavBar nb, DateTime dt) {
        nb.add(dt.toString("yyyy年MM月"), "/archive/" + dt.toString("yyyy-MM"));
    }

    private void fillSiteInfo(Map<String, Object> map) {
        //TODO 需要Cache缓存
        Map<String, Object> site = new HashMap<>();
        for (String s : new String[]{"title", "description", "copyright", "keywords", "author", "articlePageSize"}) {
            site.put(s, siteService.getString("site." + s));
        }


        Map<String, String> topNavs = new HashMap<>();
        topNavs.put("main", "站点首页");
        topNavs.put("personal", "用户中心");
        topNavs.put("about_me", "关于我们");
        topNavs.put("sitemap", "网站地图");
        site.put("topNavs", topNavs);
        site.put("hotTags", contentService.hotTag(siteService.getInteger("site.hotTagCount")));
        map.put("gl_site", site);
    }


    @Resource
    private SiteService siteService;
    @Resource
    private ContentService contentService;

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
