package com.odong.core.util;

import com.odong.core.Constants;
import com.odong.core.service.SiteService;
import com.odong.core.service.TaskService;
import com.odong.core.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by flamen on 13-12-31下午3:06.
 */
@Component("core.siteHelper")
public class SiteHelper {
    @PostConstruct
    void init() {
        if (siteService.get("site.version", String.class) == null) {
            siteService.set("site.init", new Date());
            siteService.set("site.version", Constants.VERSION);

        }
        siteService.set("site.startup", new Date());
    }

    @PreDestroy
    void destroy() {
        siteService.set("site.shutdown", new Date());
    }

    @Resource
    private TaskService taskService;
    @Resource
    private SiteService siteService;
    @Resource
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
