package com.odong.portal.controller;

import com.odong.portal.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
    String getMain(Map<String, Object> content) {

        content.put("title", "首页");
        return "main.httl";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("site.startup", siteService.get("site.startup", Date.class));
        map.put("created", new Date());
        return map;
    }
    @Resource
    private SiteService siteService;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
