package com.odong.cms.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by flamen on 14-1-5下午8:50.
 */
@Component("cms.siteHelper")
public class SiteHelper {
    @PostConstruct
    void init(){

    }
    @PreDestroy()
    void destroy{

    }
}
