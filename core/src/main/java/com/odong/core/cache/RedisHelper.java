package com.odong.core.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * Created by flamen on 13-12-30下午1:33.
 */
@Component("core.redisHelper")
public final class RedisHelper {
    public void setHash(String key, String field, Object value) {
        execute((Jedis client) -> {
            client.hset(key(key), field, object2json(value));
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getHashMap(String key) {
        return (Map) execute((Jedis client) -> {
            String k = key(key);
            Map<String, Object> map = new HashMap<>();
            for (String f : client.hkeys(k)) {
                map.put(f, json2object(client.hget(k, f)));
            }
            return map;
        });
    }

    @SuppressWarnings("unchecked")
    public List<Object> getHashSortedValues(String key) {
        return (List) execute((Jedis client) -> {
            String k = key(key);
            TreeSet<String> fields = new TreeSet<>(client.hkeys(k));
            fields.comparator();
            List<Object> list = new ArrayList<>();
            for (String f : fields) {
                list.add(json2object(client.hget(k, f)));
            }
            return list;
        });
    }

    @SuppressWarnings("unchecked")
    public Set<String> getHashFields(String key) {
        return (Set) execute((Jedis client) -> client.hkeys(key(key)));
    }

    @SuppressWarnings("unchecked")
    public List<Object> getHashValues(String key) {
        return (List) execute((Jedis client) -> {
            List<Object> list = new ArrayList<>();
            for (String json : client.hvals(key(key))) {
                list.add(json2object(json));
            }
            return list;
        });
    }

    public Object getHash(String key, String field) {
        return execute((Jedis client) -> json2object(client.hget(key(key), field)));
    }

    @SuppressWarnings("unchecked")
    public Set<String> search(String pattern) {
        return (Set<String>) execute((Jedis client) -> client.keys(key(pattern)));
    }


    public void del(String key) {
        execute((Jedis client) -> {
            client.del(key(key));
            return null;
        });
    }

    public void set(String key, Object value) {
        execute((Jedis client) -> {
            client.set(key(key), object2json(value));
            return null;
        });
    }

    public Object get(String key) {
        return execute((Jedis client) -> json2object(client.get(key(key))));
    }


    public interface Callback {
        Object execute(Jedis client);
    }


    @PostConstruct
    void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(maxConn);
        config.setMaxWait(10001);
        config.setMaxIdle(20);
        pool = new JedisPool(config, host, port);
    }

    @PreDestroy
    void destroy() {
        pool.destroy();
    }


    private String key(String key) {
        return appName + "://" + key;
    }

    private Object execute(Callback callback) {
        Jedis client = pool.getResource();
        try {
            return callback.execute(client);
        } finally {
            if (client != null) {
                pool.returnResource(client);
            }
        }
    }

    private String object2json(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }

    private Object json2object(String json) {
        return JSON.parse(json);
    }

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.max_conn}")
    private int maxConn;
    @Value("${app.name}")
    private String appName;
    private JedisPool pool;
    private final static Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }
}
