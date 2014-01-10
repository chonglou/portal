package com.odong.platform.util;

import com.odong.core.cache.CacheHelper;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.service.SiteService;
import com.odong.web.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午10:46.
 */
@Component("platform.cacheService")
public class CacheService {
    public List getArchive(int year, int month, int day) {
        return cacheHelper.get(String.format("archinve/%4d_%2d_%2d", year, month, day), ArrayList.class, null, () -> {
            ArrayList<Card> cards = new ArrayList<>();
            pluginUtil.foreach((Plugin p) -> {
                cards.addAll(p.archive(year, month, day));
            });
            return cards;
        });
    }

    public List getArchive(int year, int month) {
        return cacheHelper.get(String.format("archinve/%4d_%2d", year, month), ArrayList.class, null, () -> {
            ArrayList<Card> cards = new ArrayList<>();
            pluginUtil.foreach((Plugin p) -> {
                cards.addAll(p.archive(year, month));
            });
            return cards;
        });
    }

    public boolean isSiteAllowLogin() {
        return cacheHelper.get("site/allowLogin", Boolean.class, null, () -> siteService.get("site.allowLogin", Boolean.class));
    }

    public void popSiteAllowLogin() {
        cacheHelper.delete("site/allowLogin");
    }

    public List getLogList() {
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
    @Resource
    private PluginUtil pluginUtil;
    private final static Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
