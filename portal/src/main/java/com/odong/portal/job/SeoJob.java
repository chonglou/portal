package com.odong.portal.job;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午12:26
 */
@Component("seoJobTarget")
@DependsOn("siteHelper")
public class SeoJob {
    public void execute() {
        sitemap();
        rss();
    }

    private SyndEntry createRssSyndEntry(String domain, String url, String title, String body, Date publishedDate) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(title);
        entry.setLink(domain + url);
        entry.setPublishedDate(publishedDate);
        SyndContent aboutDesc = new SyndContentImpl();
        aboutDesc.setType("text/html");
        aboutDesc.setValue(body);
        entry.setDescription(aboutDesc);
        return entry;
    }

    private void rss() {
        try (Writer writer = new FileWriter(appStoreDir + "/seo/rss.xml")) {
            String domain = "http://" + siteService.getString("site.domain");
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle(siteService.getString("site.title"));
            feed.setLink(domain);
            feed.setDescription(siteService.getString("site.description"));
            List<SyndEntry> entries = new ArrayList<>();

            entries.add(createRssSyndEntry(domain, "/aboutMe", "关于我们", siteService.getString("site.about_me"), siteService.getDate("site.init")));

            for (Article a : contentService.listArticle()) {
                entries.add(createRssSyndEntry(domain, "/article/" + a.getId(), a.getTitle(), a.getSummary(), a.getPublishDate()));
            }

            feed.setEntries(entries);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, writer);
        } catch (IOException | FeedException e) {
            logger.error("生成RSS出错", e);
        }
    }

    private void sitemap() {

        try {
            String domain = "http://" + siteService.getString("site.domain");
            WebSitemapGenerator wsg = WebSitemapGenerator.builder(domain, new File(appStoreDir + "/seo/")).gzip(true).build();
            wsg.addUrl(new WebSitemapUrl.Options(domain + "/main").lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build());
            wsg.addUrl(new WebSitemapUrl.Options(domain + "/aboutMe").lastMod(siteService.getDate("site.init")).priority(0.9).changeFreq(ChangeFreq.YEARLY).build());
            wsg.addUrl(new WebSitemapUrl.Options(domain + "/sitemap").lastMod(new Date()).priority(0.9).changeFreq(ChangeFreq.DAILY).build());
            for (Article a : contentService.listArticle()) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/article/" + a.getId()).lastMod(a.getPublishDate()).priority(0.5).changeFreq(ChangeFreq.MONTHLY).build());
            }
            for (Tag t : contentService.listTag()) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/tag/" + t.getId()).lastMod(t.getPublishDate()).priority(0.7).changeFreq(ChangeFreq.WEEKLY).build());
            }
            for (User u : accountService.listUser()) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/user/" + u.getId()).lastMod(u.getCreated()).priority(0.7).changeFreq(ChangeFreq.WEEKLY).build());
            }
            DateTime now = new DateTime();
            for (DateTime dt = new DateTime(siteService.getDate("site.init")); dt.compareTo(now) <= 0; dt = dt.plusMonths(1)) {
                wsg.addUrl(new WebSitemapUrl.Options(domain + "/archive/" + dt.toString("yyyy-MM")).lastMod(dt.toDate()).priority(0.2).changeFreq(ChangeFreq.MONTHLY).build());
            }

            wsg.write();
        } catch (IOException e) {
            logger.error("创建网站地图出错", e);
        }
    }

    @PostConstruct
    void init() {
        execute();
    }

    @Value("${app.store}")
    private String appStoreDir;
    @Resource
    private SiteService siteService;
    @Resource
    private AccountService accountService;
    @Resource
    private ContentService contentService;
    private final static Logger logger = LoggerFactory.getLogger(SeoJob.class);

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

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
