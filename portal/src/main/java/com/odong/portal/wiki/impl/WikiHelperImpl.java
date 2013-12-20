package com.odong.portal.wiki.impl;

import com.odong.portal.redis.RedisHelper;
import com.odong.portal.wiki.model.WikiPage;
import com.odong.portal.wiki.WikiHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by flamen on 13-12-19下午8:30.
 */
@Component
public class WikiHelperImpl extends RedisHelper implements WikiHelper {
    @Override
    public Map<String, String> listPage() {
        Map<String, String> map = new HashMap<>();
        execute((Jedis client) -> {
            for (byte[] id : client.keys(key2id("*"))) {
                WikiPage p = (WikiPage) bin2obj(client.get(id));
                map.put(p.getName(), p.getTitle());
            }
            return null;
        });

        return map;
    }

    @Override
    public WikiPage getPage(String name) {
        return (WikiPage) execute((Jedis client) -> {
            byte[] k = key2id(name);

            return client.exists(k) ? bin2obj(client.get(k)) : null;

        });
    }

    @Override
    public void setPage(String name, String title, String body) {

        execute((Jedis client) -> {
            byte[] k = key2id(name);
            WikiPage page;
            if (client.exists(k)) {
                page = (WikiPage) bin2obj(client.get(k));
                page.setLastEdit(new Date());
            } else {
                page = new WikiPage();
                page.setCreated(new Date());
            }

            page.setName(name);
            page.setTitle(title);
            page.setBody(body);

            client.set(k, obj2bin(page));
            return null;
        });
    }

    @Override
    public void delPage(String name) {
        execute((Jedis client) -> client.del(key2id(name)));
    }

    @Override
    protected JedisPool getPool() {
        return pool;
    }

    @Override
    protected String getAppName() {
        return appName;
    }

    @Override
    protected String getPrefix() {
        return "wiki";
    }

    @Value("${app.name}")
    private String appName;
    @Resource
    private JedisPool pool;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }
}
