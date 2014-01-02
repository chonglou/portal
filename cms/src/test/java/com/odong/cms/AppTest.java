package com.odong.cms;

import com.odong.cms.service.ContentService;
import com.odong.core.json.JsonHelper;
import com.odong.web.model.Page;
import com.odong.web.template.TemplateHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class AppTest
{
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
            System.out.println(th.evaluate("/cms/base.httl", map));
            System.out.println("#################################################################");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

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
