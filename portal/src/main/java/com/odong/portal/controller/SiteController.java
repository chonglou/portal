package com.odong.portal.controller;

import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.web.Card;
import com.odong.portal.web.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller("c.site")
public class SiteController extends PageController {

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String getMain(Map<String, Object> map) {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "首页");
        map.put("top_nav_key", "main");

        map.put("articleList",
                cacheHelper.get(
                        "cards/leastArticle",
                        ArrayList.class,
                        null,
                        () -> {
                            ArrayList<Card> cards = new ArrayList<>();
                            contentService.latestArticle(siteService.getInteger("site.articlePageSize")).forEach((a) -> cards.add(a.toCard()));
                            return cards;
                        }
                )
        );
        map.put("defArticles",
                cacheHelper.get(
                        "cards/defArticle",
                        ArrayList.class,
                        null,
                        () -> {
                            ArrayList<Card> cards = new ArrayList<>();
                            Tag tag = contentService.getTag(siteService.getLong("site.topTag"));
                            contentService.listArticleByTag(tag.getId()).forEach((a) -> {
                                cards.add(a.toCard());
                            });
                            return cards;
                        })
        );
        return "main";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map) {
        map.put("userList", cacheHelper.get("cards/user", ArrayList.class, null, () -> {
            ArrayList<Card> cards = new ArrayList<>();
            accountService.listUser().forEach((u) -> {
                if (u.getState() == User.State.ENABLE) {
                    cards.add(new Card(u.getLogo(), u.getUsername(), u.getEmail(), "/user/" + u.getId()));
                }
            });
            return cards;
        }));
        map.put("tagList", cacheHelper.get("pages/tag", ArrayList.class, null, () -> {
            ArrayList<Page> pages = new ArrayList<>();
            contentService.listTag().forEach((t) -> pages.add(t.toPage()));
            return pages;
        }));

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "网站地图");
        map.put("top_nav_key", "sitemap");
        return "sitemap";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    String postSearch(Map<String, Object> map, HttpServletRequest request) {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        String key = request.getParameter("keyword");
        map.put("key", key);
        map.put("title", "搜索-[" + key + "]");

        //FIXME like查找性能
        map.put("articleList",
                cacheHelper.get(
                        "search/" + key,
                        ArrayList.class,
                        null, () -> {
                    ArrayList<Card> cards = new ArrayList<>();
                    contentService.search(key).forEach((a) -> {
                        cards.add(a.toCard());
                    });
                    return cards;
                })
        );
        return "search";
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "关于我们");
        map.put("top_nav_key", "aboutMe");


        map.put("logList", cacheHelper.get("logs", ArrayList.class, null, () -> {
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
        }));
        map.put("aboutMe", cacheHelper.get("aboutMe", String.class, null, () -> siteService.getString("site.aboutMe")));
        return "aboutMe";
    }


    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getError(@PathVariable int code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        switch (code) {
            case 404:
                map.put("message", "找不到文件");
                break;
            case 500:
                map.put("message", "服务器内部错误");
                break;
        }
        map.put("created", new Date());
        return map;
    }

    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

}
