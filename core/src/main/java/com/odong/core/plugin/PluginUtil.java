package com.odong.core.plugin;

import com.odong.core.json.JsonHelper;
import com.odong.core.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 14-1-2下午5:16.
 */
@Component("core.plugin.util")
public class PluginUtil implements ApplicationContextAware {
    public interface Callback {
        void execute(Plugin plugin);
    }

    public Plugin getPlugin(String name) {
        return context.getBean("plug." + name, Plugin.class);
    }

    public void foreach(Callback callback) {
        for (String name : status.keySet()) {
            if (Boolean.TRUE.equals(status.get(name))) {
                callback.execute(getPlugin(name));
            } else {
                logger.debug("模块[{}]未启用", name);
            }
        }
    }

    public synchronized void register(String name) {
        if (!status.containsKey(name)) {
            status.put(name, null);
        }
    }

    public Map<String, Boolean> status() {
        Map<String, Boolean> map = new HashMap<>();
        map.putAll(status);
        return map;
    }

    public boolean isEnable(String name) {
        return status.get(name);
    }

    public synchronized void setEnable(String name, boolean enable) {
        Plugin plugin = context.getBean("plugin." + name, Plugin.class);
        if (enable) {
            plugin.install();
        } else {
            plugin.uninstall();
        }
        status.put(name, enable);
        siteService.set("site.plugins", status);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @PostConstruct
    void init() {
        status = jsonHelper.json2map(siteService.get("site.plugins", String.class), String.class, Boolean.class);
    }

    @Resource
    private SiteService siteService;
    @Resource
    private JsonHelper jsonHelper;
    private ApplicationContext context;
    private Map<String, Boolean> status;

    private final static Logger logger = LoggerFactory.getLogger(PluginUtil.class);

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

}
