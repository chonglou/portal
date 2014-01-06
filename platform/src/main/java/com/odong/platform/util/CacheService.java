package com.odong.platform.util;

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
 * Created by flamen on 13-12-31下午10:46.
 */
@Component("platform.cacheService")
public class CacheService {
    public boolean isSiteAllowLogin() {
        return cacheHelper.get("site/allowLogin", Boolean.class, null, () -> siteService.get("site.allowLogin", Boolean.class));
    }

    public void popSiteAllowLogin() {
        cacheHelper.delete("site/allowLogin");
    }

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
