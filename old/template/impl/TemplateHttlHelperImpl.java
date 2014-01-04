package com.odong.web.template.impl;

import com.odong.core.json.JsonHelper;
import com.odong.core.service.SiteService;
import com.odong.web.template.TemplateHelper;
import httl.Engine;
import httl.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.*;

/**
 * Created by flamen on 14-1-1下午8:54.
 */

public class TemplateHttlHelperImpl implements TemplateHelper {

    @Override
    public void addPackage(String... packages) {
        this.packages.addAll(Arrays.asList(packages));
        reload();
    }

    @Override
    public void render(String view, Map<String, Object> map, OutputStream output) throws IOException, ParseException {
        Template template = engine.getTemplate(view);
        template.render(map, output);
    }

    @Override
    public String evaluate(String view, Map<String, Object> map) throws IOException, ParseException {
        Template template = engine.getTemplate(view);
        return (String) template.evaluate(map);
    }

    public void init(){
        packages = new HashSet<>();
        packages.add("com.odong.web.model");
        packages.add("com.odong.web.model.form");
        packages.add("com.odong.web.model.chart");
        packages.add("com.odong.web.model.grid");
        packages.add("com.odong.core.model");
        packages.add("com.odong.core.entity");
        reload();
    }

    private synchronized void reload() {
        Properties props = new Properties();
        props.put("input.encoding", "UTF-8");
        props.put("output.encoding", "UTF-8");
        props.put("reloadable", debug ? "true" : "false");
        props.put("precompiled", debug ? "false" : "true");
        props.put("localized", "false");
        props.put("template.suffix", ".httl");
        props.put("cache.capacity", "10240");
        props.put("loaders", "httl.spi.loaders.ClasspathLoader");
        props.put("template.directory", "/templates/httl");
        //props.put("import.macros", "/core/macros.httl");
        props.put("import.packages+", StringUtils.join(packages, ","));

        logger.debug("HTTL SCAN List:{}", packages);
        engine = Engine.getEngine(props);
    }

    private Engine engine;
    private boolean debug;
    private Set<String> packages;
    private final static Logger logger = LoggerFactory.getLogger(TemplateHttlHelperImpl.class);


    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
