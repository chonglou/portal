package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
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
    @RequestMapping(value = "/{year}/{month}/{day}", method = RequestMethod.GET)
    String getArchiveByDay(Map<String, Object> map, @PathVariable int year, @PathVariable int month, @PathVariable int day) {

        List<NavBar> navBars = new ArrayList<>();
        navBars.add(cacheService.getArchiveNavBar());
        map.put("navBars", navBars);
        map.put("articleList", cacheService.getArticleCardByDay(year, month, day));
        fillSiteInfo(map);
        map.put("title", new DateTime().withYear(year).withMonthOfYear(month).toString("yyyy年MM月dd日"));
        return "cms/archive";
    }

    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.GET)
    String getArchiveByMonth(Map<String, Object> map, @PathVariable int year, @PathVariable int month) {

        List<NavBar> navBars = new ArrayList<>();

        navBars.add(cacheService.getArchiveNavBar());

        map.put("navBars", navBars);
        map.put("articleList", cacheService.getArticleCardByMonth(year, month));
        fillSiteInfo(map);
        map.put("title", new DateTime().withYear(year).withMonthOfYear(month).toString("yyyy年MM月"));
        return "cms/archive";
    }

}
