package com.odong.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by flamen on 13-12-31下午10:39.
 */
@EnableWebMvc
@Configuration("platform.config.web")
public class Web extends WebMvcConfigurerAdapter {
/*
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorPathExtension(true) //扩展名支持 /user/{userid}.json
                //.favorParameter(true) //参数支持 /user/{userid}?format=json
                //.parameterName("mediaType")
                .ignoreAcceptHeader(true) //忽略掉header
                .useJaf(false)
                .defaultContentType(MediaType.TEXT_HTML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON);

    }
    */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/main");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/jquery/**").addResourceLocations("/WEB-INF/jquery/");
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("/WEB-INF/bootstrap-3.0.0/");
        registry.addResourceHandler("/ueditor/**").addResourceLocations("/WEB-INF/ueditor1_3_5-utf8/");
        registry.addResourceHandler("/jquery-ui/**").addResourceLocations("/WEB-INF/jquery-ui-1.10.3/");
        registry.addResourceHandler("/highcharts/**").addResourceLocations("/WEB-INF/Highcharts-3.0.1/");
        registry.addResourceHandler("/style/**").addResourceLocations("/WEB-INF/style/");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
        registry.addResourceHandler("/3rd/**").addResourceLocations("/WEB-INF/3rd/");
        registry.addResourceHandler("/video-js/**").addResourceLocations("/WEB-INF/video-js-4.3.0/");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
