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
@Configuration("portal.web")
public class Web extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/main");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/jquery/**").addResourceLocations("/WEB-INF/jquery/");
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("/WEB-INF/bootstrap-2.3.2/");
        registry.addResourceHandler("/kindeditor/**").addResourceLocations("/WEB-INF/kindeditor-4.1.7/");
        registry.addResourceHandler("/jcloud/**").addResourceLocations("/WEB-INF/jcloud/");
        registry.addResourceHandler("/style/**").addResourceLocations("/WEB-INF/style/");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
