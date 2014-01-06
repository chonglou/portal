package com.odong.cms.controller;

import com.odong.web.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by flamen on 14-1-5下午8:46.
 */
@Controller("cms.c.site")
public class SiteController {
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map, HttpSession session) {
        Page page = formHelper.getPage(session);
        map.put("page", page);
        return "/platform/main";
    }



    @RequestMapping(value = "/page/{pgId}", method = RequestMethod.GET)
    String getPage(Map<String, Object> map, @PathVariable int pgId) {
        pager(map, pgId);
        map.put("title", "第" + pgId + "页");
        return "cms/page";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map, HttpSession session) {
        Page page = formHelper.getPage(session);
        page.setTitle("网站地图");
        page.setIndex("/sitemap");
        map.put("page", page);
        //FIXME cache ?
        map.put("cards", siteHelper.getSitemapCards());
        map.put("links", siteHelper.getSitemapLinks());
        return "platform/sitemap";
    }


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



    @RequestMapping(value = "/search", method = RequestMethod.POST)
    String postSearch(Map<String, Object> map, HttpServletRequest request, HttpSession session) {
        Page page = formHelper.getPage(session);
        String key = request.getParameter("keyword");
        page.setTitle("搜索-[" + key + "]的结果");
        map.put("page", page);
        //FIXME google?
        return "search";
        /*
        Date last = (Date) session.getAttribute("lastSearch");
        session.setAttribute("lastSearch", new Date());
        if (last == null || last.getTime() + 1000 * siteService.get("search.space", Integer.class) < new Date().getTime()) {

            return "search";
        } else {
            ResponseItem item = new ResponseItem(ResponseItem.Type.message);
            item.addData("过于频繁的搜索，请过" + searchSpace + "秒钟重试");
            map.put("item", item);
            return "message";
        }
        */
    }



    private void pager(Map<String, Object> map, int index) {
        //FIXME 从cache中读取数据
        /*
        fillSiteInfo(map);
        map.put("top_nav_key", "main");
        map.put("navBars", getNavBars());

        Pager pager = cacheService.getPager();
        if (index < 1) {
            index = 1;
        } else if (pager.getTotal() > 0 && index > pager.getTotal()) {
            index = pager.getTotal();
        }
        pager.setIndex(index);
        map.put("pager", pager);

        map.put("articleList", cacheService.getArticleCardsByPager(index, pager.getSize()));
        */
    }
}
