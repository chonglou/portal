package com.odong.portal.job;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午12:26
 */
@Component("sitemapJobTarget")
public class SitemapJob {
    public void execute() {

        try {
            String domain = "http://" + siteService.getString("site.domain");
            WebSitemapGenerator wsg = WebSitemapGenerator.builder(domain, new File(appStoreDir + "/seo")).gzip(true).build();
            wsg.addUrl(new WebSitemapUrl.Options(domain + "/main").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build());
            wsg.addUrl(new WebSitemapUrl.Options(domain + "/about_me").lastMod(siteService.getDate("site.init")).priority(0.9).changeFreq(ChangeFreq.WEEKLY).build());
            for (Article a : contentService.listArticle()) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/article/" + a.getId()).lastMod(a.getPublishDate()).priority(0.5).changeFreq(ChangeFreq.DAILY).build());
            }
            for (Tag t : contentService.listTag()) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/tag/" + t.getId()).lastMod(t.getPublishDate()).priority(0.7).changeFreq(ChangeFreq.WEEKLY).build());
            }
            wsg.write();
        } catch (IOException e) {
            logger.error("创建网站地图出错", e);
        }
    }

    @PostConstruct
    void init() {

    }

    @Value("${app.store}")
    private String appStoreDir;
    @Resource
    private SiteService siteService;
    @Resource
    private ContentService contentService;
    private final static Logger logger = LoggerFactory.getLogger(SitemapJob.class);

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }
}
