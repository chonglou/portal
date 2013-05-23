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
        siteService.setObject("site.startup", new Date());
        if (siteService.getObject("site.init", Date.class) == null) {
            siteService.setObject("site.init", new Date());
            siteService.setObject("site.version", "v20130522");
            siteService.setObject("site.key", stringHelper.random(512));
            siteService.setObject("site.title", "门户网站系统");
            siteService.setObject("site.copy_right", "");
            siteService.setObject("site.allow_register", true);
            siteService.setObject("site.allow_login", true);
        }
    }

    @PreDestroy
    void destroy() {
        siteService.setObject("site.shutdown", new Date());
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
