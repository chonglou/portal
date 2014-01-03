package com.odong.cms.controller;

import com.odong.portal.entity.cms.Statics;
import com.odong.portal.service.ContentService;
import com.odong.portal.util.CacheService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-9.
 */
@Controller("c.cms.statics")
public class StaticsController {

    @RequestMapping(value = "/player/{id}", method = RequestMethod.GET)
    String getPlayer(Map<String, Object> map, @PathVariable int id) {
        Statics s = contentService.getStatics(id);
        map.put("url", s.getUrl());
        map.put("type", "flv");
        map.put("title", s.getName());
        return "player";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getStatus() {
        HashMap info = cacheService.getSiteInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("title", info.get("title"));
        map.put("description", info.get("description"));
        map.put("aboutMe", cacheService.getAboutMe());
        map.put("version", info.get("apkVersion"));
        return map;
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    @ResponseBody
    List<Statics> getVideoList() {
        return contentService.listStatics(Statics.Type.VIDEO);
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    @ResponseBody
    List<Statics> getBookList() {
        return contentService.listStatics(Statics.Type.BOOK);
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    @ResponseBody
    Statics getBook(@PathVariable int id) {
        return contentService.getStatics(id);
    }

    @Resource
    private ContentService contentService;
    @Resource
    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }
}
