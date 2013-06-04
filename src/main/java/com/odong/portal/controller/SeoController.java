package com.odong.portal.controller;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.rss.RssContent;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:59
 */
@Controller
public class SeoController {
    @RequestMapping(value = "/sitemap.xml.gz", method = RequestMethod.GET)
    void getSitemap(HttpServletRequest request, HttpServletResponse response) throws IOException {


    }

    @RequestMapping(value = "/rss.xml", method = RequestMethod.GET)
    ModelAndView getRss() {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("rssView");
        mav.addObject("feedTitle", siteService.getString("site.title"));
        mav.addObject("feedDesc", siteService.getString("site.description"));
        String domain = "http://" + siteService.getString("site.domain");
        mav.addObject("feedLink", domain);

        List<RssContent> items = new ArrayList<>();
        items.add(new RssContent("关于我们", siteService.getString("site.aboutMe"), domain + "/about_me", null, siteService.getDate("site.init")));

        for (Tag t : contentService.listTag()) {
            RssContent rc = new RssContent();
            rc.setPublish(t.getPublishDate());
            rc.setTitle("标签-" + t.getName());
            rc.setSummary("标签[" + t.getName() + "]的文章列表");
            rc.setUrl(domain + "/tag/" + t.getId());
            items.add(rc);
        }
        for (Article a : contentService.listArticle()) {
            RssContent rc = new RssContent();
            rc.setPublish(a.getPublishDate());
            rc.setTitle(a.getTitle());
            rc.setSummary(a.getSummary());
            rc.setUrl(domain + "/article/" + a.getId());
            rc.setAuthor(accountService.getUser(rc.getAuthor()).getEmail());
            items.add(rc);
        }

        mav.addObject("feedContent", items);
        return mav;
    }

    @Resource
    private ContentService contentService;
    @Resource
    private SiteService siteService;
    @Resource
    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
