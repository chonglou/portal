package com.odong.portal.config;

import net.rubyeye.xmemcached.CommandFactory;
import net.rubyeye.xmemcached.MemcachedSessionLocator;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 上午12:31
 */
@Configuration
public class Cache {
    @Bean(name = "memcachedClientBuilder")
    XMemcachedClientBuilder getClient(@Qualifier CommandFactory commandFactory,
                                      @Qualifier MemcachedSessionLocator sessionLocator,
                                      @Qualifier Transcoder transcoder) {
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);
        builder.setConnectionPoolSize(poolSize);
        builder.setCommandFactory(commandFactory);
        builder.setSessionLocator(sessionLocator);
        builder.setTranscoder(transcoder);
        return builder;
    }

    @Bean
    Transcoder getTranscoder() {
        return new SerializingTranscoder();
    }

    @Bean
    MemcachedSessionLocator getSessionLocator() {
        return new KetamaMemcachedSessionLocator();
    }

    @Bean
    CommandFactory getCommandFactory() {
        return new BinaryCommandFactory();
    }

    @Value("${memcached.servers}")
    private String servers;
    @Value("${memcached.pool_size}")
    private int poolSize;

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }


}
