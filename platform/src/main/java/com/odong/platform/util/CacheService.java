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

    public String getAboutMe(){
        return cacheHelper.get("platform/aboutMe", String.class, null, ()->siteService.get("site.aboutMe", String.class));
    }
    public void popAboutMe(){
        cacheHelper.delete("platform/aboutMe");
    }

    public QqAuthProfile getQqAuthProfile(){
        return cacheHelper.get("platform/oauth/qq", QqAuthProfile.class, null, ()->siteService.get("site.oauth.qq", QqAuthProfile.class, true));
    }
    public void popQqAuthProfile(){
        cacheHelper.delete("platform/oauth/qq");
    }
    public GoogleAuthProfile getGoogleAuthProfile(){
        return cacheHelper.get("platform/oauth/google", GoogleAuthProfile.class, null, ()->siteService.get("site.oauth.google", GoogleAuthProfile.class, true));
    }
    public void popGoogleAuthProfile(){
        cacheHelper.delete("platform/oauth/google");
    }
    public String getGoogleValidCode() {
        return cacheHelper.get("platform/googleValidCode", String.class, null, () -> siteService.get("site.googleValidCode", String.class));
    }


    public void popGoogleValidCode() {
        cacheHelper.delete("platform/googleValidCode");
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
