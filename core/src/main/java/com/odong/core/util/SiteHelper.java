package com.odong.core.util;

import com.odong.core.cache.RedisHelper;
import com.odong.core.service.SiteService;
import com.odong.web.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;

/**
 * 统计站点信息
 * Created by flamen on 14-1-5下午2:37.
 */
@Component("core.siteHelper")
public class SiteHelper {

    public void addRss(RssItem item){
        redisHelper.setHash("rss", item.getUrl(), item);
    }
    @SuppressWarnings("unchecked")
    public List<RssItem> getRss(){
        return (List)redisHelper.getHashValues("rss");
    }
    public void addSitemap(SitemapItem item){
        redisHelper.setHash("sitemap", item.getUrl(), item);
    }
    @SuppressWarnings("unchecked")
    public List<SitemapItem> getSitemap(){
        return (List)redisHelper.getHashValues("sitemap");
    }
    public void setSideBars(int index, SideBar sideBar){
        redisHelper.setHash("sitebars", Integer.toString(index), sideBar);
    }
    @SuppressWarnings("unchecked")
    public List<SideBar> getSideBar(){
        return (List)redisHelper.getHashSortedValues("sitebars");
    }
    public void setNavBar(int index, Link link){
        redisHelper.setHash("navBar", Integer.toString(index), link);
    }
    @SuppressWarnings("unchecked")
    public List<Link> getNavBar(){
        return (List)redisHelper.getHashSortedValues("navBar");
    }

    public void addTagCloud(Link link){
        redisHelper.setHash("tagCloud", link.getUrl(), link);
    }
    @SuppressWarnings("unchecked")
    public List<Link> getTagCloud(){
        return (List)redisHelper.getHashValues("tagCloud");
    }


    public void addSitemapLinks(String name, List<Link> links) {
        redisHelper.setHash("sitemap/links", name, links);
    }

    public void addSitemapCards(String name, List<Card> cards) {
        redisHelper.setHash("sitemap/cards", name, cards);
    }
    @SuppressWarnings("unchecked")
    public Map<String, List<Link>> getSitemapLinks() {
        return (Map)redisHelper.getHashMap("sitemap/links");
    }
    @SuppressWarnings("unchecked")
    public Map<String, List<Card>> getSitemapCards() {
        return (Map) redisHelper.getHashMap("sitemap/cards");
    }

    @PostConstruct
    void init() {
        siteService.set("site.startup", new Date());
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }


    @Resource
    private RedisHelper redisHelper;

    @Resource
    private SiteService siteService;

    public void setRedisHelper(RedisHelper redisHelper) {
        this.redisHelper = redisHelper;
    }


    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


}
