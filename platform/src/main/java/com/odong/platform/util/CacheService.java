package com.odong.platform.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.SiteService;
import com.odong.platform.util.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by flamen on 13-12-31下午10:46.
 */
@Component("platform.cacheService")
public class CacheService  {


    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;
    private final static Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
