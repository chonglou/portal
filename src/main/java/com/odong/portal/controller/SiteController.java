package com.odong.portal.controller;

import com.odong.portal.service.SiteService;
import com.odong.portal.web.ResponseItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller
public class SiteController {
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map) {
        Map<String, String> site = new HashMap<>();
        for (String s : new String[]{"title", "description", "copyright", "keywords", "author"}) {
            site.put(s, siteService.getString("site." + s));
        }
        map.put("gl_site", site);
        return "main.httl";
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
    Map<String,Object> getError(@PathVariable int code){
        Map<String,Object> map = new HashMap<>();
        map.put("code", code);
        switch (code){
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
