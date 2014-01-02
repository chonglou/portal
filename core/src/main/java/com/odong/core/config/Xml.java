package com.odong.core.config;

import com.odong.web.model.ResponseItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午7:56.
 */
@Configuration("core.config.xml")
public class Xml {
    @Bean(name = "core.xStreamMarshaller")
    public XStreamMarshaller getxStreamMarshaller(){
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String,Class<?>> map = new HashMap<>();
        map.put("ResponseItem", ResponseItem.class);
        marshaller.setAliases(map);
        return marshaller;
    }
}
