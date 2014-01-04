package com.odong.cms.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.service.SiteService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by flamen on 14-1-4上午12:26.
 */
@Component("cms.cacheService")
public class CacheService {
    public String getApkVersion(){
        return cacheHelper.get("cms/apk.version", String.class, null, ()->siteService.get("apk.version", String.class));
    }
    public void popApkVersion(){
        cacheHelper.delete("cms/apk.version");
    }
    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;
    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
