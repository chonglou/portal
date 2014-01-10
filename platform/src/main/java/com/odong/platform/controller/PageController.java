package com.odong.platform.controller;

import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.util.FormHelper;
import com.odong.platform.util.CacheService;
import com.odong.web.model.Card;
import com.odong.web.model.Link;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 14-1-10下午2:04.
 */
@Controller("platform.c.page")
public class PageController {

    @RequestMapping(value = "/archive/{year}/{month}/{day}", method = RequestMethod.GET)
    String getArchiveByDay(Map<String, Object> map, @PathVariable int year, @PathVariable int month, @PathVariable int day, HttpSession session) {
        Page page = formHelper.getPage(session);
        map.put("page", page);

        map.put("cards", cacheService.getArchive(year, month, day));
        map.put("title", String.format("%4d年%2d月%2d日", year, month, day));
        return "/platform/archive";
    }

    @RequestMapping(value = "/archive/{year}/{month}", method = RequestMethod.GET)
    String getArchiveByMonth(Map<String, Object> map, @PathVariable int year, @PathVariable int month, HttpSession session) {
        Page page = formHelper.getPage(session);
        map.put("page", page);

        map.put("cards", cacheService.getArchive(year, month));
        map.put("title", String.format("%4d年%2d月", year, month));
        return "/platform/archive";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    String postSearch(Map<String, Object> map, HttpServletRequest request, HttpSession session) {
        Date last = (Date) session.getAttribute("lastSearch");
        session.setAttribute("lastSearch", new Date());
        int searchSpace = coreCacheService.getSearchSpace();
        Page page = formHelper.getPage(session);
        map.put("page", page);
        if (last == null || last.getTime() + 1000 * searchSpace < new Date().getTime()) {
            String key = request.getParameter("keyword");
            map.put("title", "搜索-[" + key + "]的结果");


            Map<String, List<Card>> cards = new HashMap<>();
            pluginUtil.foreach((Plugin plugin) -> {
                cards.putAll(plugin.getSitemapCards());
            });

            map.put("cards", cards);
            return "/platform/search";
        } else {
            ResponseItem item = new ResponseItem(ResponseItem.Type.message);
            item.addData("过于频繁的搜索，请过" + searchSpace + "秒钟重试");
            map.put("title", "出错了");
            map.put("message", item);
            return "/core/message";
        }
    }

    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map, HttpSession session) {
        Page page = formHelper.getPage(session);
        page.setIndex("/sitemap");
        map.put("page", page);
        map.put("title", "网站地图");
        Map<String, List<Card>> cards = new HashMap<>();
        Map<String, List<Link>> links = new HashMap<>();
        pluginUtil.foreach((Plugin plugin) -> {
            cards.putAll(plugin.getSitemapCards());
            links.putAll(plugin.getSitemapLinks());
        });

        map.put("cards", cards);
        map.put("links", links);
        return "/platform/sitemap";
    }


    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map, HttpSession session) {

        Page page = formHelper.getPage(session);
        page.setIndex("/aboutMe");
        map.put("page", page);
        map.put("logList", cacheService.getLogList());
        map.put("aboutMe", coreCacheService.getAboutMe());
        map.put("title", "关于我们");
        return "/platform/aboutMe";
    }

    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    String getError(@PathVariable int code, Map<String, Object> map, HttpSession session) {
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
        Page page = formHelper.getPage(session);
        map.put("title", "提示信息");
        map.put("page", page);
        map.put("message", item);
        return "/core/message";
    }

    private final static Logger logger = LoggerFactory.getLogger(PageController.class);

    @Resource
    private CacheService cacheService;
    @Resource
    private PluginUtil pluginUtil;
    @Resource
    private FormHelper formHelper;
    @Resource
    private com.odong.core.util.CacheService coreCacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setCoreCacheService(com.odong.core.util.CacheService coreCacheService) {
        this.coreCacheService = coreCacheService;
    }
}
