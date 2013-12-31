package com.odong.core;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * 模块接口定义
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
    void init();

    /**
     * 卸载清理
     */
    void destroy();

    /**
     * @return 当前状态 null:未安装 True:启用 False:禁用
     */
    Boolean isEnable();

    /**
     * 设置状态
     *
     * @param enable true:启动用 false:禁用
     */
    void setEnable(boolean enable);

    /**
     * 处理定时任务
     * @param message 任务信息
     * @throws JMSException
     */
    void onMessage(MapMessage message) throws JMSException;
}
