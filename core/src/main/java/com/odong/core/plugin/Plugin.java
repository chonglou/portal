package com.odong.core.plugin;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * 模块接口定义
 * 命名规则plugin.module_name
 * Created by flamen on 13-12-30上午2:42.
 */
public interface Plugin {
    /**
     * @return 名称
     */
    String name();

    /**
     * @return 标题
     */
    String title();

    /**
     * @return 帮助信息（HTML格式）
     */
    String help();

    /**
     * 初始化安装
     */
    void install();

    /**
     * 卸载清理
     */
    void uninstall();

    /**
     * 处理定时任务
     *
     * @param message 任务信息
     * @throws JMSException
     */
    void onMessage(MapMessage message) throws JMSException;
}
