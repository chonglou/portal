package com.odong.portal.util;

import com.odong.portal.service.SiteService;
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
@Component
public class SiteHelper {
    @PostConstruct
    void init() {
        siteService.set("site.startup", new Date());
        if (siteService.get("site.init") == null) {
            siteService.set("site.init", new Date());
            siteService.set("site.version", "v20130522");
            siteService.set("site.key", encryptHelper.random(512));
            siteService.set("site.title", "门户网站系统");
            siteService.set("site.copy_right", "");
            siteService.set("site.allow_register", true);
            siteService.set("site.allow_login", true);

        }
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }

    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private SiteService siteService;

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
