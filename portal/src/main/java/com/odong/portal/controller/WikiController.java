package com.odong.portal.controller;

import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextAreaField;
import com.odong.portal.web.form.TextField;
import com.odong.portal.wiki.WikiHelper;
import com.odong.portal.wiki.model.WikiPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-19下午11:46.
 */
@Controller("c.wiki")
@RequestMapping(value = "/wiki")
@SessionAttributes(SessionItem.KEY)
public class WikiController extends PageController {
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    String getWiki(Map<String, Object> map, HttpServletRequest request) {
        String name = getName(request);
        if (name.equals("")) {
            name = "index";
        }


        WikiPage page = wikiHelper.getPage(name);

        if (page == null) {
            page = new WikiPage();
            page.setName(name);
            map.put("title", name);
        } else {
            map.put("title", page.getTitle());
        }
        map.put("page", page);


        List<NavBar> navBars = new ArrayList<>();
        navBars.add(cacheService.getWikiNavBar());
        map.put("navBars", navBars);
        fillSiteInfo(map);
        map.put("top_nav_key", "wiki/");
        return "wiki";
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    Form putWiki(@RequestParam String name,
                 @ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("add", "编辑页面[" + name + "]", "/wiki/");
        if (si.isSsAdmin()) {
            WikiPage wp = wikiHelper.getPage(name);
            if (wp == null) {
                wp = new WikiPage();
            }
            fm.addField(new TextField<>("name", "名称", name));
            fm.addField(new TextField<>("title", "标题", wp.getTitle()));
            TextAreaField taf = new TextAreaField("body", "内容", wp.getBody());
            taf.setHtml(true);
            fm.addField(taf);
            fm.setOk(true);
        } else {
            fm.addData("没有权限");
        }
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postWiki(@RequestParam String name,
                          @RequestParam String title,
                          @RequestParam String body,
                          @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        if (si.isSsAdmin()) {
            wikiHelper.setPage(name, title, body);
            ri.setOk(true);
        } else {
            ri.addData("没有权限");
        }

        return ri;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem delWiki(@RequestParam String name, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (si.isSsAdmin()) {
            wikiHelper.delPage(name);
            ri.setOk(true);
        } else {
            ri.addData("没有权限");
        }

        return ri;
    }

    private String getName(HttpServletRequest request) {
        return request.getRequestURI().substring("/wiki/".length());
    }

    @Resource
    private WikiHelper wikiHelper;

    public void setWikiHelper(WikiHelper wikiHelper) {
        this.wikiHelper = wikiHelper;
    }
}
