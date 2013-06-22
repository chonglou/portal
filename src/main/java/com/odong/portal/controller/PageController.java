package com.odong.portal.controller;

import com.odong.portal.model.SessionItem;
import com.odong.portal.service.SiteService;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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
@Controller
@SessionAttributes(SessionItem.KEY)
public class PageController {

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
        return "main";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map) {
        fillSiteInfo(map);
        map.put("title", "首页");
        return "main";
    }

    private void fillSiteInfo(Map<String, Object> map){
        Map<String, String> site = new HashMap<>();
        for (String s : new String[]{"title", "description", "copyright", "keywords", "author"}) {
            site.put(s, siteService.getString("site." + s));
        }
        map.put("gl_site", site);

    }


    @Resource
    private SiteService siteService;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
