package com.odong.portal.controller;

import com.odong.portal.model.SessionItem;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.wiki.Page;
import com.odong.portal.wiki.WikiHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by flamen on 13-12-19下午11:46.
 */
@Controller("c.wiki")
@RequestMapping(value = "/wiki")
@SessionAttributes(SessionItem.KEY)
public class WikiController {
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    @ResponseBody
    ResponseItem getWiki(HttpServletRequest request){

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        ri.addData(request.getRequestURI().substring("/wiki/".length()));
        ri.addData(wikiHelper.listPage());
        ri.setOk(true);
        return ri;
    }
    /*
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseBody
    ResponseItem getWiki(@PathVariable String name){
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        Page page = wikiHelper.getPage(name);
        if(page == null){
            wikiHelper.setPage(name, "标题"+name, "内容"+name);
            page = wikiHelper.getPage(name);
        }
        ri.addData(page);
        return ri;
    }
    */
    @Resource
    private WikiHelper wikiHelper;

    public void setWikiHelper(WikiHelper wikiHelper) {
        this.wikiHelper = wikiHelper;
    }
}
