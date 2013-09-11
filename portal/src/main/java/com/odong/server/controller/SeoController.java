package com.odong.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:59
 */
@Controller("c.seo")
public class SeoController {
    @RequestMapping(value = "/sitemap.xml.gz", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource sitemap() {
        return new FileSystemResource(appStoreDir + "/seo/sitemap.xml.gz");
    }

    @RequestMapping(value = "/rss.xml", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource rss() {
        return new FileSystemResource(appStoreDir + "/seo/rss.xml");
    }

    @Value("${app.store}")
    private String appStoreDir;

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

}
