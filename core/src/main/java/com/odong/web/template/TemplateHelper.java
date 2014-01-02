package com.odong.web.template;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午8:53.
 */

public interface TemplateHelper {
    void render(Map<String,Object> map, HttpServletResponse response)throws IOException, ParseException;
}
