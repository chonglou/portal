package com.odong.core;

import com.odong.core.cache.CacheHelper;
import com.odong.core.entity.Log;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.*;
import com.odong.web.model.Link;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.*;
import com.odong.web.template.TemplateHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppTest {


    //@Test
    public void testForm(){
        Map<String,Object> map = new HashMap<>();
        Form fm = new Form("install", "系统初始化", "/install");
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
        fm.addField(new AgreeField("agree", "用户协议", "协议内容"));
        fm.setCaptcha(true);
        fm.setOk(true);
        map.put("form", fm);

        template("/core/form.httl", map);

    }


    @Test
    public void testPage(){

        Map<String,Object> map = new HashMap<>();
        Page page = new Page();
        page.setDebug(true);
        page.setTitle("标题");
        page.setAuthor("zhengjitang@gmail.com");
        page.setCopyright("&copy;2013");
        page.setDescription("说明信息");
        page.setCaptcha("kaptcha");

        page.getTopLinks().add(new Link("/aaa", "aaa"));
        page.getTopLinks().add(new Link("/bbb", "bbb"));
        page.getTopLinks().add(new Link("/ccc", "ccc"));
        page.setIndex("/ccc");

        QqAuthProfile qqAuth = new QqAuthProfile();
        qqAuth.setEnable(true);
        page.setQqAuth(qqAuth);

        map.put("page", page);
        map.put("title", "title");

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        for(int i=0; i<5;i++){
            ri.addData("提示信息"+i);
        }
        map.put("message", ri);
        template("/core/message.httl", map);
    }



    //@Test
    public void testUser() {
        try {
            UserService us = ctx.getBean(UserService.class);
            log(us.listUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testRbac() {
        try {

            RbacService rs = ctx.getBean(RbacService.class);
            rs.bind("aaa", "bbb", "ccc", new Date(), new Date(), true);
            rs.bind("aaa", "bbb", "ccc", new Date(), new Date(), false);
            rs.bind("aaa", "bbb", "ccc1", new Date(), new Date(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void testCache() {
        try {
            CacheHelper cacheHelper = ctx.getBean(CacheHelper.class);
            cacheHelper.set("aaa", 30, "bbb");
            log(cacheHelper.status(), cacheHelper.get("aaa", String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testTask() {
        try {
            TaskService taskService = ctx.getBean(TaskService.class);
            taskService.addTask(null, "GC", "", 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testSetting() {
        try {
            SiteService siteService = ctx.getBean(SiteService.class);
            siteService.set("sss", "ddd");
            log(siteService.get("sss", String.class));
            siteService.set("sss", "eee");
            log(siteService.get("sss", String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testLog() {
        try {
            LogService logService = ctx.getBean(LogService.class);
            /*
            for(Log l : logService.list(1l, 20l, 10)){
                log(l);
            }

            for(Log l : logService.list(1l, 120l, 50)){
                log(l);
            }
            */

            for (int i = 0; i < 5; i++) {
                logService.add(1l, "测试啊", Log.Type.INFO);
            }
            log(logService.count(1l));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @BeforeTest
    public void init() {
        try {
            ctx = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterTest
    public void destroy() {

    }

    private void log(Object... objects) {
        System.out.println("########################################################");
        JsonHelper jsonHelper = ctx.getBean(JsonHelper.class);
        for (Object obj : objects) {
            System.out.println(jsonHelper.object2json(obj));
        }
    }

    private void template(String view, Map<String, Object> map){
        try{
            TemplateHelper th = ctx.getBean(TemplateHelper.class);
            System.out.println("#################################################################");
            System.out.println(th.evaluate(view, map));
            System.out.println("#################################################################");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private ApplicationContext ctx;

}
