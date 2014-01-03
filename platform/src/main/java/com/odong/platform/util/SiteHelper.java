package com.odong.platform.util;

import com.odong.core.service.SiteService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by flamen on 13-12-30下午9:44.
 */
@Component("platform.util.site")
public class SiteHelper {
    @PostConstruct
    void init() {
        siteService.set("site.startup", new Date());
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }

    @Resource
    private SiteService siteService;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

}