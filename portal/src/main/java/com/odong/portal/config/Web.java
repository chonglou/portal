package com.odong.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:26
 */
@EnableWebMvc
@Configuration("config.web")
public class Web extends WebMvcConfigurerAdapter {
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
        registry.addResourceHandler("/jcarousel/**").addResourceLocations("/WEB-INF/jcarousel/");


    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
