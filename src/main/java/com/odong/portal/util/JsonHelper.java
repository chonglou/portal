package com.odong.portal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:27
 */
@Component
public class JsonHelper {
    public String object2json(Object object){
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e){
            logger.error("输出JSON出错", e);
        }
        return "{}";
    }
    public <T> T json2object(String json, Class<T> clazz){
        try{
        mapper.readValue(json, clazz);
        }
        catch (IOException e){
            logger.debug("解析JSON出错",e);
        }
        return null;
    }
    public <K,V> Map<K,V> json2list(String json, Class<K> kClazz, Class<V> vClazz){
        try{
        return mapper.readValue(json, new TypeReference<Map<K,V>>(){});
        }
        catch (IOException e){
            logger.debug("解析JSON出错",e);
        }
        return new HashMap<>();
    }

    public <T> List<T> json2List(String json, Class<T> clazz){
        try{
        return mapper.readValue(json, new TypeReference<List<T>>(){});
        }
        catch (IOException e){
            logger.debug("解析JSON出错",e);
        }
        return new ArrayList<>();
    }
    @PostConstruct
    void init(){
        mapper = new ObjectMapper();
    }
    private ObjectMapper mapper;
    private final static Logger logger = LoggerFactory.getLogger(JsonHelper.class);
}
