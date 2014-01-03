package com.odong.platform.controller.personal;

import com.odong.portal.controller.PageController;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
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
    @RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getItems(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<Page> pages = new ArrayList<>();
        if (session.getAttribute(SessionItem.KEY) == null) {
            pages.add(new Page("用户登录", "/personal/login"));
            pages.add(new Page("账户注册", "/personal/register"));
            pages.add(new Page("账户激活", "/personal/active"));
            pages.add(new Page("重置密码", "/personal/resetPwd"));
            map.put("ok", false);
        } else {
            SessionItem si = (SessionItem) session.getAttribute(SessionItem.KEY);
            pages.add(new Page("用户中心", "/personal/self"));
            pages.add(new Page("安全退出", "/personal/logout"));
            map.put("logo", si.getSsLogo());
            map.put("name", si.getSsUsername());
            map.put("type", si.getSsType());
            map.put("ok", true);
        }

        map.put("items", pages);

        return map;
    }


    @RequestMapping(value = "/self", method = RequestMethod.GET)
    String getIndex(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        List<NavBar> navBars = new ArrayList<>();

        NavBar nbInfo = new NavBar("个人信息");
        nbInfo.add("基本信息", "/personal/info");
        if (si.isSsLocal()) {
            nbInfo.add("更改密码", "/personal/setPwd");
        }
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
            nbSite.add("静态资源", "/admin/statics/");
            nbSite.add("站点信息", "/admin/site/info");
            nbSite.add("用户协议", "/admin/site/regProtocol");
            nbSite.add("站点状态", "/admin/state/");
            nbSite.add("分页设置", "/admin/pager/");
            nbSite.add("定时任务", "/admin/task/");
            nbSite.add("邮件设置", "/admin/smtp/");
            nbSite.add("分享代码", "/admin/share/");
            nbSite.add("广告代码", "/admin/advert/");
            nbSite.add("友情链接", "/admin/friendLink/");
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
        map.put("manager", manager);
        map.put("articleList", si.isSsAdmin() ? contentService.listArticle() : contentService.listArticleByAuthor(si.getSsUserId()));
        return "personal/article";
    }

    @Resource
    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
