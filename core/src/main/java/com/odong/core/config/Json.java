package com.odong.core.config;

import com.odong.core.cache.CacheHelper;
import com.odong.core.cache.impl.CacheEhcacheHelperImpl;
import com.odong.core.cache.impl.CacheMemcachedHelperImpl;
import com.odong.core.cache.impl.CacheRedisHelperImpl;
import com.odong.core.json.JsonHelper;
import com.odong.core.json.impl.JsonFastjsonHelperImpl;
import com.odong.core.json.impl.JsonJacksonHelperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by flamen on 13-12-30下午12:19.
 */
@Configuration("core.json")
public class Json {

}
