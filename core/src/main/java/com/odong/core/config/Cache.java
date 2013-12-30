package com.odong.core.config;

import com.odong.core.cache.CacheHelper;
import com.odong.core.cache.impl.CacheEhcacheHelperImpl;
import com.odong.core.cache.impl.CacheMemcachedHelperImpl;
import com.odong.core.cache.impl.CacheRedisHelperImpl;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-30下午1:14.
 */
@Configuration("core.cache")
public class Cache {

}
