package com.odong.portal.util;

import com.odong.portal.service.SiteService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午5:08
 */
@Component
public class MailHelper {

    @PostConstruct
    void init(){

    }
    @Resource
    private SiteService siteService;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
