package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Article;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午3:09
 */
@Controller("c.cms.archive")
@RequestMapping(value = "/archive")
@SessionAttributes(SessionItem.KEY)
public class ArchiveController extends PageController {


    @RequestMapping(value = "/{year}-{month}", method = RequestMethod.GET)
    String getArchive(Map<String, Object> map, @PathVariable int year, @PathVariable int month) {

        List<NavBar> navBars = new ArrayList<>();

        NavBar nbArchive = new NavBar("归档列表");
        nbArchive.setType(NavBar.Type.LIST);
        DateTime now = new DateTime();
        for (DateTime init = new DateTime(siteService.getDate("site.init")); init.compareTo(now) <= 0; init = init.plusMonths(1)) {
            addArchive2NavBar(nbArchive, init);
        }
        navBars.add(nbArchive);

        map.put("navBars", navBars);
        //TODO 分页
        List<Article> articles = contentService.listArticleByMonth(year, month);
        map.put("articleList", articles);
        map.put("userMap", getUserMap(articles));
        fillSiteInfo(map);
        map.put("title", new DateTime().withYear(year).withMonthOfYear(month).toString("yyyy年MM月"));
        return "cms/archive";
    }
}
