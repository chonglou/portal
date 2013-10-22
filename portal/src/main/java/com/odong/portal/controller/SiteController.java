package com.odong.portal.controller;

import com.odong.portal.web.Pager;
import com.odong.portal.web.ResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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
        pager(map, 1);
        map.put("title", "首页");
        map.put("defArticles", cacheService.getArticleCardsByTag(cacheService.getTopTag()));
        return "main";
    }

    @RequestMapping(value = "/page/{pgId}", method = RequestMethod.GET)
    String getPage(Map<String, Object> map, @PathVariable int pgId) {
        pager(map, pgId);
        map.put("title", "第" + pgId + "页");
        return "cms/page";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map) {
        map.put("userList", cacheService.getUserCards());
        map.put("tagList", cacheService.getTagPages());
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        map.put("title", "网站地图");
        map.put("top_nav_key", "sitemap");
        return "sitemap";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    String postSearch(Map<String, Object> map, HttpServletRequest request, HttpSession session) {
        Date last = (Date) session.getAttribute("lastSearch");
        session.setAttribute("lastSearch", new Date());

        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        String key = request.getParameter("keyword");
        map.put("key", key);
        map.put("title", "搜索-[" + key + "]");

        if (last == null || last.getTime() + 1000 * searchSpace < new Date().getTime()) {
            //FIXME like查找性能
            map.put("articleList", cacheService.getArticleCardsBySearch(key));
            map.put("google", cacheService.getGoogleSearch());
            return "search";
        } else {
            ResponseItem item = new ResponseItem(ResponseItem.Type.message);
            item.addData("过于频繁的搜索，请过" + searchSpace + "秒钟重试");
            map.put("item", item);
            return "message";
        }
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "关于我们");
        map.put("top_nav_key", "aboutMe");


        map.put("logList", cacheService.getLogList());
        map.put("aboutMe", cacheService.getAboutMe());
        return "aboutMe";
    }


    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    String getError(@PathVariable int code, Map<String, Object> map) {
        ResponseItem item = new ResponseItem(ResponseItem.Type.message);
        switch (code) {
            case 400:
                item.addData("错误的请求");
                break;
            case 404:
                item.addData("资源不存在");
                break;
            case 500:
                item.addData("服务器内部错误");
                break;
            default:
                item.addData("未知错误[" + code + "]");
                break;
        }
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        map.put("item", item);
        return "message";
    }
    /*
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
    */


    @RequestMapping(value = "/google*.html", method = RequestMethod.GET)
    void getGoogleValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vCode = cacheService.getGoogleValidCode();
        logger.debug("##### {} {}", vCode, request.getRequestURI().substring(1));
        if (request.getRequestURI().substring(1).equals(vCode)) {
            response.getWriter().println("google-site-verification: " + vCode);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @RequestMapping(value = "/sessionId", method = RequestMethod.POST)
    @ResponseBody
    String getSessionId(HttpSession session) {
        return session.getId();
    }


    private void pager(Map<String, Object> map, int index) {
        fillSiteInfo(map);
        map.put("top_nav_key", "main");
        map.put("navBars", getNavBars());

        Pager pager = cacheService.getPager();
        if (index < 1) {
            index = 1;
        } else if (pager.getTotal()>0 && index > pager.getTotal()) {
            index = pager.getTotal();
        }
        pager.setIndex(index);
        map.put("pager", pager);

        map.put("articleList", cacheService.getArticleCardsByPager(index, pager.getSize()));
    }

    @Value("${search.space}")
    private int searchSpace;
    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

    public void setSearchSpace(int searchSpace) {
        this.searchSpace = searchSpace;
    }
}
