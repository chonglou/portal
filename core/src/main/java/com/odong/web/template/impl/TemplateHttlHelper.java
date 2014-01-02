package com.odong.web.template.impl;

import com.odong.web.template.TemplateHelper;
import httl.Engine;
import httl.Template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by flamen on 14-1-1下午8:54.
 */

public class TemplateHttlHelper implements TemplateHelper {

    @Override
    public void render(Map<String, Object> map, HttpServletResponse response) throws IOException, ParseException{
        response.setContentType("text/html");
        Template template = engine.getTemplate("/install.httl");
        template.render(map, response.getOutputStream());

    }

    private void render(String view, Map<String, Object> map, HttpServletResponse response){

    }

    public void init(){
        engine = Engine.getEngine();
    }

    private Engine engine;

}
