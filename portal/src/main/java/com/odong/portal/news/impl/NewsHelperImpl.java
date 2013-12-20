package com.odong.portal.news.impl;

import com.odong.portal.news.NewsHelper;
import com.odong.portal.news.model.News;
import com.odong.portal.redis.RedisHelper;
import com.odong.portal.web.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by flamen on 13-12-20上午1:53.
 */
@Component
public class NewsHelperImpl extends RedisHelper implements NewsHelper {
    @Override
    public void add(String title, String body, String source, String url) {

        execute((Jedis client)->{
            News n = new News();
            n.setTitle(title);
            n.setBody(body);
            n.setSource(source);
            n.setUrl(url);
            n.setCreated(new Date());
            n.setVersion(1);
           client.lpush(key, obj2bin(n));
            return null;
        });
    }

    @Override
    public void set(int index, String title, String body, String source, String url) {
        execute((Jedis client)->{
            News n = (News)bin2obj(client.lindex(key, index));
            n.setTitle(title);
            n.setBody(body);
            n.setSource(source);
            n.setUrl(url);
            n.setLastEdit(new Date());
            n.setVersion(n.getVersion()+1);
            client.lset(key, index, obj2bin(n));
            return null;
        });
    }

    @Override
    public int count() {
        return (Integer)execute((Jedis client)->client.llen(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Page> page(int start, int stop) {
        return (List<Page>)execute((Jedis client)->client.lrange(key, start, stop));
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
        return "news";
    }
    @PostConstruct
    void init(){
        key = key2id("list");
    }
    @Resource
    private JedisPool pool;
    @Value("${app.name}")
    private String appName;
    private byte[] key;

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
