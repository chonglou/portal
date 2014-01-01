package com.odong.platform.util.impl;

import com.odong.core.cache.CacheHelper;
import com.odong.core.service.SiteService;
import com.odong.platform.util.CacheService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-31下午10:46.
 */
@Component("platform.cacheService")
public class CacheServiceImpl implements CacheService {
    @Override
    public String getGoogleValidCode() {
        return cacheHelper.get("site/googleValidCode", String.class, null, ()->siteService.get("site.googleValidCode", String.class));
    }

    @Override
    public void popGoogleValidCode() {
        cacheHelper.delete("site/googleValidCode");
    }

    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
