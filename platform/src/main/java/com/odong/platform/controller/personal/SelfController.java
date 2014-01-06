package com.odong.platform.controller.personal;

import com.odong.core.entity.User;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.service.LogService;
import com.odong.core.util.FormHelper;
import com.odong.web.model.Link;
import com.odong.web.model.SessionItem;
import com.odong.web.model.SideBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class SelfController {
    @RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getItems(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<Link> pages = new ArrayList<>();
        SessionItem si = formHelper.getSessionItem(session);
        if (si == null) {
            pages.add(new Link("用户登录", "/personal/login"));
            pages.add(new Link("账户注册", "/personal/register"));
            pages.add(new Link("账户激活", "/personal/active"));
            pages.add(new Link("重置密码", "/personal/resetPwd"));
            map.put("ok", false);
        } else {
            pages.add(new Link("用户中心", "/personal/self"));
            pages.add(new Link("安全退出", "/personal/logout"));
            map.put("logo", si.getSsLogo());
            map.put("name", si.getSsUsername());
            map.put("type", si.getSsType());
            map.put("ok", true);
        }
        map.put("items", pages);
        return map;
    }


    @RequestMapping(value = "/self", method = RequestMethod.GET)
    String getIndex(Map<String, Object> map, HttpSession session) {
        SessionItem si = formHelper.getSessionItem(session);
        List<SideBar> navBars = new ArrayList<>();
        SideBar nbInfo = new SideBar("个人信息");
        nbInfo.add("基本信息", "/personal/info");
        if (si.getSsType() == User.Type.EMAIL) {
            nbInfo.add("更改密码", "/personal/setPwd");
        }
        nbInfo.add("日志管理", "/personal/log");
        nbInfo.setAjax(true);
        navBars.add(nbInfo);

        pluginUtil.foreach((Plugin plugin) -> {
            navBars.add(plugin.getPersonalSideBar());
        });

        if (si.isSsAdmin()) {
            SideBar nbSite = new SideBar("站点管理");
            nbSite.add("插件管理", "/admin/plugin/");
            nbSite.add("运行状态", "/admin/state/");
            nbSite.add("站点信息", "/admin/site/");
            nbSite.add("用户管理", "/admin/user/");
            nbSite.add("定时任务", "/admin/task/");
            nbSite.add("邮件设置", "/admin/smtp/");
            nbSite.add("分享代码", "/admin/share/");
            nbSite.add("广告代码", "/admin/advert/");
            nbSite.add("存储管理", "/admin/store/");
            nbSite.add("验证码", "/admin/captcha/");

            pluginUtil.foreach((Plugin plugin) -> {
                if (plugin.getAdminSideBarLinks() != null) {
                    nbSite.addSplitter();
                    for (Link l : plugin.getAdminSideBarLinks()) {
                        nbSite.getLinks().add(l);
                    }
                }
            });

            nbSite.setAjax(true);
            navBars.add(nbSite);
        }
        map.put("navBars", navBars);

        map.put("title", "用户中心");
        map.put("top_nav_key", "personal/self");
        return "/platform/personal/self";
    }


    @RequestMapping(value = "/log", method = RequestMethod.GET)
    String getLog(Map<String, Object> map, HttpSession session) {
        map.put("logList", logService.least(formHelper.getSessionItem(session).getSsUserId(), 100));
        return "/platform/personal/log";
    }


    @Resource
    private LogService logService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private PluginUtil pluginUtil;

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
