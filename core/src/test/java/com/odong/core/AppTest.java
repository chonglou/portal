package com.odong.core;

import com.odong.core.cache.CacheHelper;
import com.odong.core.entity.Log;
import com.odong.core.json.JsonHelper;
import com.odong.core.service.*;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import com.odong.web.template.TemplateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AppTest {


    @Test
    public void testTemplate(){
        try{
            Page page = new Page();
            page.setDebug(true);
            page.setTitle("标题");
            page.setAuthor("zhengjitang@gmail.com");
            page.setCopyright("@2013");
            page.setDescription("说明信息");
            page.setCaptcha("kaptcha");

            TemplateHelper th = ctx.getBean(TemplateHelper.class);
            Map<String,Object> map = new HashMap<>();
            map.put("glPage", page);
            map.put("title", "title");
            //map.put("glMain", th.evaluate("/widgets/form.httl", new HashMap<String, Object>()));


            System.out.println("#################################################################");
            System.out.println(th.evaluate("/core/base.httl", map));
            System.out.println("#################################################################");
        }
        catch (Exception e){
            e.printStackTrace();
        }
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

    private ApplicationContext ctx;

}
