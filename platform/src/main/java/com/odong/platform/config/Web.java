package com.odong.platform.config;

import com.odong.web.resolver.JsonViewResolver;
import com.odong.web.view.rss.RssView;
import httl.web.springmvc.HttlViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午10:39.
 */
@EnableWebMvc
@Configuration("platform.config.web")
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
        registry.addResourceHandler("/3rd/**").addResourceLocations("/WEB-INF/3rd/");
        registry.addResourceHandler("/video-js/**").addResourceLocations("/WEB-INF/video-js-4.3.0/");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}
