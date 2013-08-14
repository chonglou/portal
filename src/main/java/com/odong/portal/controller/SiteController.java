package com.odong.portal.controller;

import com.odong.portal.entity.Tag;
import com.odong.portal.service.SiteService;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        map.put("title", "关于我们");
        map.put("top_nav_key", "aboutMe");
        return "aboutMe";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("site.startup", siteService.getObject("site.startup", Date.class));
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

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
