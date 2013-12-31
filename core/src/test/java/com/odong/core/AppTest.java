package com.odong.core;

import com.odong.core.cache.CacheHelper;
import com.odong.core.entity.Log;
import com.odong.core.json.JsonHelper;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.store.DbUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {
    @Test
    public void testCache() {
        try{
            CacheHelper cacheHelper = ctx.getBean(CacheHelper.class);
            cacheHelper.set("aaa", 30, "bbb");
            log(cacheHelper.status(), cacheHelper.get("aaa", String.class));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSetting(){
        try{
            SiteService siteService = ctx.getBean(SiteService.class);
            siteService.put("sss", "ddd");
            log(siteService.get("sss"));
            siteService.put("sss", "eee");
            log(siteService.get("sss"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
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

            for(int i=0; i<5; i++){
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
