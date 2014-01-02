package com.odong.web.template.impl;

import com.odong.web.template.TemplateHelper;
import httl.Engine;
import httl.Template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by flamen on 14-1-1下午8:54.
 */

public class TemplateHttlHelperImpl implements TemplateHelper {

    @Override
    public void render(String view, Map<String, Object> map, OutputStream output) throws IOException, ParseException{
        Template template = engine.getTemplate(view);
        template.render(map, output);
    }

    @Override
    public String evaluate(String view, Map<String, Object> map) throws IOException, ParseException {
        Template template = engine.getTemplate(view);
        return (String)template.evaluate(map);
    }


    public void init(){
        engine = Engine.getEngine();
    }

    private Engine engine;

}
