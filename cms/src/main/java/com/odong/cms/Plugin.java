package com.odong.cms;

import com.odong.core.plugin.PluginUtil;
import com.odong.web.template.TemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * Created by flamen on 14-1-2下午5:30.
 */
@Component("plugin.cms")
public class Plugin implements com.odong.core.plugin.Plugin {
    @Override
    public String name() {
        return "cms";
    }

    @Override
    public String title() {
        return "内容管理";
    }

    @Override
    public String help() {
        return "帮助信息";
    }

    @Override
    public void install() {
        logger.info("安装模块[{}]", name());
    }

    @Override
    public void uninstall() {
        logger.info("卸载模块[{}]", name());
    }
    @PostConstruct
    void init(){
        pluginUtil.register(name());
        templateHelper.addPackage("com.odong.cms.entity","com.odong.cms.model");
    }

    @Override
    public void onMessage(MapMessage message) throws JMSException {
        logger.debug("丢弃的消息[{}]", message);
    }
    @Resource
    private TemplateHelper templateHelper;
    @Resource
    private PluginUtil pluginUtil;
    private final static Logger logger = LoggerFactory.getLogger(Plugin.class);

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setTemplateHelper(TemplateHelper templateHelper) {
        this.templateHelper = templateHelper;
    }
}
