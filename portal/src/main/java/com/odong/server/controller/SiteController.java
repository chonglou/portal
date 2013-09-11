package com.odong.server.controller;

import com.odong.server.entity.Article;
import com.odong.server.service.ContentService;
import com.odong.server.service.SiteService;
import com.odong.server.util.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller("c.site")
public class SiteController extends PageController {

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map) {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "首页");
        map.put("top_nav_key", "main");
        //TODO 分页
        map.put("articleList", contentService.latestArticle(siteService.getInteger("site.articlePageSize")));
        map.put("defArticle", contentService.getArticle(siteService.getString("site.defArticle")));

        return "main";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map) {

        //TODO 分页
        map.put("articleList", contentService.listArticle());
        map.put("userList", accountService.listUser());
        map.put("tagList", contentService.listTag());

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "网站地图");
        map.put("top_nav_key", "sitemap");
        return "sitemap";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    String postSearch(Map<String, Object> map, HttpServletRequest request) {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        String key = request.getParameter("keyword");
        map.put("key", key);
        map.put("title", "搜索-[" + key + "]");

        //FIXME like查找性能
        map.put("articleList",
                cacheHelper.get(
                        "search/"+key,
                        (Class<List<Article>>) Collections.<Article>emptyList().getClass(),
                        3600*24, ()->contentService.search(key))
        );
        return "search";
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "关于我们");
        map.put("top_nav_key", "aboutMe");
        List<String> logList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/Change-Logs")))) {
            String line;
            while ((line = br.readLine()) != null) {
                logList.add(line);
            }
        } catch (IOException e) {
            logger.error("加载大事记文件出错", e);
        }
        map.put("logList", logList);
        map.put("aboutMe", siteService.getString("site.aboutMe"));
        return "aboutMe";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("site.startup", siteService.getObject("site.startup", Date.class));
        map.put("site.cache", cacheHelper.status());
        map.put("created", new Date());
        return map;
    }

    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getError(@PathVariable int code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        switch (code) {
            case 404:
                map.put("message", "找不到文件");
                break;
            case 500:
                map.put("message", "服务器内部错误");
                break;
        }
        map.put("created", new Date());
        return map;
    }

    @Resource
    private SiteService siteService;
    @Resource
    private ContentService contentService;
    @Resource
    private CacheHelper cacheHelper;
    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
