package com.odong.portal.util;

import com.odong.portal.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:41
 */
@Component("siteHelper")
public class SiteHelper {
    @PostConstruct
    void init() {
        siteService.set("site.startup", new Date());
        if (siteService.getObject("site.init", Date.class) == null) {
            siteService.set("site.init", new Date());
            siteService.set("site.version", "v20130522");
            siteService.set("site.key", stringHelper.random(512));
            siteService.set("site.title", "门户网站系统");
            siteService.set("site.copyright", "&copy;2013");
            siteService.set("site.allowRegister", true);
            siteService.set("site.allowLogin", true);
            siteService.set("site.author", "zhengjitang@gmail.com");
        }
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }

    @Resource
    private StringHelper stringHelper;
    @Resource
    private SiteService siteService;
    private final static Logger logger = LoggerFactory.getLogger(SiteHelper.class);

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
