package com.odong.cms;

import com.odong.cms.service.ContentService;
import com.odong.core.json.JsonHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest
{
    @Test
    public void testCms(){
        try{
            ContentService cs = ctx.getBean(ContentService.class);
        }
        catch (Exception e){
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
