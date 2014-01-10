package com.odong.platform.controller;

import com.odong.core.job.TaskSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

/**
 * Created by flamen on 14-1-10下午12:17.
 */
@Controller("platform.c.seo")
public class SeoController {
    @RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource robots() {
        return new FileSystemResource(appStoreDir + "/seo/robots.txt");
    }

    @RequestMapping(value = "/sitemap.xml.gz", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource sitemap() {
        return new FileSystemResource(mapF);
    }

    @RequestMapping(value = "/rss.xml", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource rss() {
        return new FileSystemResource(rssF);
    }

    @RequestMapping(value = "/site.png", method = RequestMethod.GET)
    @ResponseBody
    FileSystemResource qrCode() {
        return new FileSystemResource(qrF);
    }

    @PostConstruct
    void init() {
        rssF = appStoreDir + "/seo/rss.xml";
        mapF = appStoreDir + "/seo/sitemap.xml.gz";
        qrF = appStoreDir + "/site.png";

        if (!new File(rssF).exists()) {
            taskSender.send(null, "rss", null);
        }
        if (!new File(mapF).exists()) {
            taskSender.send(null, "sitemap", null);
        }
        if (!new File(qrF).exists()) {
            taskSender.send(null, "qrcode", null);
        }
    }

    private String rssF;
    private String mapF;
    private String qrF;
    @Value("${app.store}")
    private String appStoreDir;
    @Resource
    private TaskSender taskSender;

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }
}
