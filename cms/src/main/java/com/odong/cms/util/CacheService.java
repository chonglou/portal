package com.odong.cms.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by flamen on 14-1-4上午12:26.
 */
@Component("cms.cacheService")
public class CacheService {
    public ArrayList getLogList() {
        return cacheHelper.get("logs", ArrayList.class, null, () -> {
            ArrayList<String> logList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/Change-Logs")))) {
                String line;
                while ((line = br.readLine()) != null) {
                    logList.add(line);
                }
            } catch (IOException e) {
                logger.error("加载大事记文件出错", e);
            }
            return logList;
        });
    }




    public void popGoogleValidCode() {
        cacheHelper.delete("platform/googleValidCode");
    }
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
    private final static Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
