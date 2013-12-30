package com.odong.core;

import com.odong.core.cache.CacheHelper;
import com.odong.core.json.JsonHelper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;

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
    public void testHttpClient() {
        try {
            log(new Date());

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
        JsonHelper jsonHelper = ctx.getBean(JsonHelper.class);
        for (Object obj : objects) {
            System.out.println(jsonHelper.object2json(obj));
        }
    }

    private ApplicationContext ctx;

}
