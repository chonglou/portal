package com.odong.portal.controller.personal;

import com.odong.portal.controller.PageController;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller("c.personal.self")
@RequestMapping(value = "/personal")
@SessionAttributes(SessionItem.KEY)
public class SelfController extends PageController {

    @RequestMapping(value = "/self", method = RequestMethod.GET)
    String getIndex(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        List<NavBar> navBars = new ArrayList<>();

        NavBar nbInfo = new NavBar("个人信息");
        nbInfo.add("基本信息", "/personal/info");
        nbInfo.add("更改密码", "/personal/setPwd");
        nbInfo.add("日志管理", "/personal/log");
        nbInfo.setAjax(true);
        navBars.add(nbInfo);


        NavBar nbCms = new NavBar("内容管理");
        nbCms.add("文章管理", "/personal/article");
        nbCms.add("评论管理", "/personal/comment");
        nbCms.setAjax(true);
        navBars.add(nbCms);

        if (si.isSsAdmin()) {
            NavBar nbSite = new NavBar("站点管理");
            nbSite.add("用户管理", "/admin/user/");
            nbSite.add("标签管理", "/admin/tag/");
            nbSite.add("站点信息", "/admin/site/info");
            nbSite.add("用户协议", "/admin/site/regProtocol");
            nbSite.add("站点状态", "/admin/state/");
            nbSite.add("分页设置", "/admin/site/pager");
            nbSite.add("定时任务", "/admin/task/");
            nbSite.add("邮件设置", "/admin/smtp/");
            nbSite.add("友情链接", "/admin/friendLink/");
            nbSite.add("广告设置", "/admin/advert/");
            nbSite.add("关于我们", "/admin/aboutMe/");
            nbSite.add("验证码", "/admin/captcha/");
            nbSite.add("数据库", "/admin/database/");
            nbSite.setAjax(true);
            navBars.add(nbSite);
        }
        map.put("navBars", navBars);
        fillSiteInfo(map);
        map.put("title", "用户中心");
        map.put("top_nav_key", "personal/self");
        return "personal/self";
    }


    @RequestMapping(value = "/log", method = RequestMethod.GET)
    String getLog(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("logList", logService.list(si.getSsUserId(), 100));
        return "personal/log";
    }

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    String getComment(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("commentList", contentService.listCommentByUser(si.getSsUserId()));
        return "personal/comment";
    }

    @RequestMapping(value = "/article", method = RequestMethod.GET)
    String getArticle(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("articleList", si.isSsAdmin() ?  contentService.listArticle(): contentService.listArticleByAuthor(si.getSsUserId()));
        return "personal/article";
    }

    @Resource
    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
