package com.odong.web.template;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午8:53.
 */

public interface TemplateHelper {
    void addPackage(String... packages);
    void render(String view, Map<String, Object> map, OutputStream output) throws IOException, ParseException;

    String evaluate(String view, Map<String, Object> map) throws IOException, ParseException;
}
