package com.odong.platform.controller;

import com.odong.core.Constants;
import com.odong.core.service.SiteService;
import com.odong.core.service.TaskService;
import com.odong.core.service.UserService;
import com.odong.platform.util.CacheService;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.*;
import com.odong.web.template.TemplateHelper;
import httl.Engine;
import httl.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by flamen on 13-12-30下午9:40.
 */
@Controller("platform.c.site")
public class SiteController {
    //@RequestMapping(value = "/install", method = RequestMethod.GET)
    String getInstall(Map<String, Object> map, HttpServletResponse response) throws IOException {
        if (siteService.get("site.version", String.class) == null) {
            map.put("title", "PORTAL系统安装");
            map.put("author", Constants.AUTHOR);
            map.put("copyright", Constants.COPYRIGHT);
        } else {
            response.sendRedirect("/main");
        }
        return "install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.GET)
    void getInstall(HttpServletResponse response) throws IOException, ParseException{
        Map<String, Object> map = new HashMap<>();
        map.put("title", "测试标题");
        templateHelper.render(map, response);
    }

    Form getInstall(Map<String, Object> map) {
        Form fm = new Form("install", "系统初始化", "/install");
        if (siteService.get("site.version", String.class) == null) {
            logger.debug("数据库尚未初始化");
            fm.addField(new SplitterField("站点信息"));
            fm.addField(new TextField<String>("domain", "域名"));
            TextField<String> keywords = new TextField<>("keywords", "关键字");
            keywords.setWidth(800);
            fm.addField(keywords);
            TextAreaField description = new TextAreaField("description", "说明信息");
            description.setHtml(false);
            fm.addField(description);

            fm.addField(new SplitterField("管理员信息"));
            fm.addField(new TextField<String>("email", "管理员邮箱"));
            fm.addField(new TextField<String>("password", "登录密码"));

            fm.addField(new SplitterField("邮件系统"));
            fm.addField(new TextField<>("smtpHost", "主机"));
            fm.addField(new TextField<>("smtpPort", "端口"));
            fm.addField(new TextField<>("smtpUsername", "用户名"));
            fm.addField(new TextField<>("smtpPassword", "密码"));
            RadioField<Boolean> ssl = new RadioField<>("smtpSsl", "启用SSL", false);

            ssl.addOption("是", true);
            ssl.addOption("否", false);
            fm.addField(ssl);
            TextField<String> bcc = new TextField<>("smtpBcc", "密送");
            bcc.setRequired(false);
            fm.addField(bcc);

            fm.addField(new SplitterField("其他"));
            fm.addField(new AgreeField("agree", "用户协议", agreement));
            fm.setCaptcha(true);
            fm.setOk(true);
        } else {
            fm.addData("已经安装");
        }
        return fm;
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    ResponseItem getMain() {
        //pager(map, 1);
        //map.put("title", "首页");
        //map.put("defArticles", cacheService.getArticleCardsByTag(cacheService.getTopTag()));
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        return ri;
    }

    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    String getError(@PathVariable int code, Map<String, Object> map) {
        ResponseItem item = new ResponseItem(ResponseItem.Type.message);
        switch (code) {
            case 400:
                item.addData("错误的请求");
                break;
            case 404:
                item.addData("资源不存在");
                break;
            case 500:
                item.addData("服务器内部错误");
                break;
            default:
                item.addData("未知错误[" + code + "]");
                break;
        }
        return "message";
    }

    /*
    ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (siteService.get("site.version", String.class) == null) {

            siteService.set("site.init", new Date());
            siteService.set("site.author", "zhengjitang@gmail.com");
            siteService.set("site.version", Constants.VERSION);
            ri.setOk(true);
        }
        else {
            ri.addData("已经初始化");
        }
    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem getInstall(HttpServletResponse response) throws IOException{
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (siteService.get("site.version", String.class) == null) {

            siteService.set("site.init", new Date());
            siteService.set("site.author", "zhengjitang@gmail.com");
            siteService.set("site.version", Constants.VERSION);
            ri.setOk(true);
        }
        else {
            ri.addData("已经初始化");
        }
    }
    */


/*
    @RequestMapping(value = "/page/{pgId}", method = RequestMethod.GET)
    String getPage(Map<String, Object> map, @PathVariable int pgId) {
        pager(map, pgId);
        map.put("title", "第" + pgId + "页");
        return "cms/page";
    }


    @RequestMapping(value = "sitemap", method = RequestMethod.GET)
    String getSitemap(Map<String, Object> map) {
        map.put("userList", cacheService.getUserCards());
        map.put("tagList", cacheService.getTagPages());
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        map.put("title", "网站地图");
        map.put("top_nav_key", "sitemap");
        return "sitemap";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    String postSearch(Map<String, Object> map, HttpServletRequest request, HttpSession session) {
        Date last = (Date) session.getAttribute("lastSearch");
        session.setAttribute("lastSearch", new Date());

        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        String key = request.getParameter("keyword");
        map.put("key", key);
        map.put("title", "搜索-[" + key + "]");

        if (last == null || last.getTime() + 1000 * searchSpace < new Date().getTime()) {
            //FIXME like查找性能
            map.put("articleList", cacheService.getArticleCardsBySearch(key));
            map.put("google", cacheService.getGoogleSearch());
            return "search";
        } else {
            ResponseItem item = new ResponseItem(ResponseItem.Type.message);
            item.addData("过于频繁的搜索，请过" + searchSpace + "秒钟重试");
            map.put("item", item);
            return "message";
        }
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map) {

        map.put("navBars", getNavBars());
        fillSiteInfo(map);

        map.put("title", "关于我们");
        map.put("top_nav_key", "aboutMe");


        map.put("logList", cacheService.getLogList());
        map.put("aboutMe", cacheService.getAboutMe());
        return "aboutMe";
    }



    */
    /*
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
    */


    @RequestMapping(value = "/google*.html", method = RequestMethod.GET)
    void getGoogleValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vCode = cacheService.getGoogleValidCode();
        logger.debug("##### {} {}", vCode, request.getRequestURI().substring(1));
        if (request.getRequestURI().substring(1).equals(vCode)) {
            response.getWriter().println("google-site-verification: " + vCode);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @RequestMapping(value = "/sessionId", method = RequestMethod.POST)
    @ResponseBody
    String getSessionId(HttpSession session) {
        return session.getId();
    }


    /*
    private void pager(Map<String, Object> map, int index) {
        fillSiteInfo(map);
        map.put("top_nav_key", "main");
        map.put("navBars", getNavBars());

        Pager pager = cacheService.getPager();
        if (index < 1) {
            index = 1;
        } else if (pager.getTotal() > 0 && index > pager.getTotal()) {
            index = pager.getTotal();
        }
        pager.setIndex(index);
        map.put("pager", pager);

        map.put("articleList", cacheService.getArticleCardsByPager(index, pager.getSize()));
    }
    */

    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

    @Resource
    private CacheService cacheService;

    @Resource
    private SiteService siteService;
    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;
    @Value("${app.agreement}")
    private String agreement;
    @Resource
    private TemplateHelper templateHelper;

    public void setTemplateHelper(TemplateHelper templateHelper) {
        this.templateHelper = templateHelper;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
