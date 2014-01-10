package com.odong.core.plugin;

import com.odong.web.model.*;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.List;
import java.util.Map;

/**
 * 模块接口定义
 * <p>
 * 命名规则plugin.module_name
 * Created by flamen on 13-12-30上午2:42.
 */
public interface Plugin {

    List<RssItem> rss();

    List<SitemapItem> sitemap();

    List<Card> search(String key);

    List<Card> archive(int year, int month);
    List<Card> archive(int year, int month, int day);

    List<Link> getTagCloud();

    List<SideBar> getSideBars();

    Map<String, List<Card>> getSitemapCards();

    Map<String, List<Link>> getSitemapLinks();

    List<Link> getTopLinks();

    List<Link> getAdminSideBarLinks();

    SideBar getPersonalSideBar();


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
