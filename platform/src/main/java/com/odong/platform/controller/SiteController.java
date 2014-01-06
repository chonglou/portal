package com.odong.platform.controller;

import com.odong.core.Constants;
import com.odong.core.entity.User;
import com.odong.core.model.SmtpProfile;
import com.odong.core.service.SiteService;
import com.odong.core.service.TaskService;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.core.util.SiteHelper;
import com.odong.platform.form.InstallForm;
import com.odong.platform.util.CacheService;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-30下午9:40.
 */
@Controller("platform.c.site")
public class SiteController {

    @RequestMapping(value = "/install", method = RequestMethod.GET)
    String getInstall(Map<String, Object> map, HttpServletResponse response) throws IOException, ParseException {
        if (siteService.get("site.version", String.class) == null) {
            map.put("title", "PORTAL系统安装");
            map.put("author", Constants.AUTHOR);
            map.put("copyright", Constants.COPYRIGHT);
        } else {
            response.sendRedirect("/main");
        }
        return "platform/install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.PUT)
    @ResponseBody
    Form putInstall(){
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
            fm.addField(new TextField<String>("username", "用户名"));
            fm.addField(new TextField<String>("email", "邮箱"));
            fm.addField(new TextField<String>("password", "登录密码"));

            fm.addField(new SplitterField("邮件系统"));
            fm.addField(new TextField<String>("smtpHost", "主机"));
            fm.addField(new TextField<String>("smtpPort", "端口"));
            fm.addField(new TextField<String>("smtpUsername", "用户名"));
            fm.addField(new TextField<String>("smtpPassword", "密码"));
            fm.addField(new TextField<String>("smtpFrom","发送者"));
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

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postInstall(@Valid InstallForm form, BindingResult result, HttpServletRequest request) throws IOException{
        ResponseItem ri = formHelper.check(result, request, true);
        if(!ri.isOk()){
            return ri;
        }
        if (siteService.get("site.version", String.class) != null) {
            ri.setOk(false);
            ri.addData("已经初始化");
            return ri;
        }
        logger.info("设置站点信息");
        siteService.set("site.init", new Date());
        siteService.set("site.author", "zhengjitang@gmail.com");
        siteService.set("site.domain", form.getDomain());
        siteService.set("site.keywords", form.getKeywords());
        siteService.set("site.description", form.getDescription());
        siteService.set("search.space", 30);

        logger.info("设置管理员信息");
        long uid = userService.addEmailUser(form.getEmail(), form.getUsername(), form.getPassword());
        userService.setUserState(uid, User.State.ENABLE);

        logger.info("设置SMTP信息");
        SmtpProfile sp = new SmtpProfile(form.getSmtpHost(),  form.getUsername(), form.getPassword(), form.getSmtpBcc());
        sp.setPort(form.getSmtpPort());
        sp.setSsl(form.isSmtpSsl());
        sp.setFrom(form.getSmtpFrom());
        siteService.set("site.smtp", sp, true);

        logger.info("设置定时任务");
       siteService.set("task.gc",taskService.addTask(null, "gc", null, 2));
        siteService.set("task.backup",taskService.addTask(null, "backup", null, 3));
        siteService.set("task.rss",taskService.addTask(null, "rss", null, 4));
        siteService.set("task.sitemap",taskService.addTask(null, "sitemap", null, 4));
        logger.info("安装完毕");
        siteService.set("site.version", Constants.VERSION);
        ri.setOk(true);
        return ri;
    }

    @RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
    String getError(@PathVariable int code, Map<String, Object> map, HttpSession session) {
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
        Page page = formHelper.getPage(session);
        map.put("page", page);
        map.put("message", item);
        return "/core/message.httl";
    }


    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    String getAboutMe(Map<String, Object> map, HttpSession session) {

        Page page = formHelper.getPage(session);
        page.setTitle("关于我们");
        page.setIndex("/aboutMe");
        map.put("page", page);
        map.put("logList", cacheService.getLogList());
        map.put("aboutMe", coreCacheService.getAboutMe());
        return "platform/aboutMe";
    }

    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);


    @Resource
    private com.odong.core.util.CacheService coreCacheService;
    @Resource
    private CacheService cacheService;
    @Resource
    private SiteService siteService;
    @Resource
    private UserService userService;
    @Resource
    private TaskService taskService;
    @Resource
    private FormHelper formHelper;
    @Value("${app.agreement}")
    private String agreement;

    public void setCoreCacheService(com.odong.core.util.CacheService coreCacheService) {
        this.coreCacheService = coreCacheService;
    }


    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
