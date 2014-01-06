package com.odong.platform.util;

import com.odong.core.util.TimeHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by flamen on 14-1-6下午12:05.
 */
@Component("platform.rbacService")
public class RbacService {
    public boolean isAdmin(long userId) {
        return rbacService.auth("user://" + userId, "manager", "resource://site");
    }

    public void setAdmin(long userId, boolean enable) {
        rbacService.bind("user://" + userId, "manager", "resource://site", new Date(), timeHelper.max(), enable);
    }

    @Resource
    private com.odong.core.service.RbacService rbacService;
    @Resource
    private TimeHelper timeHelper;

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setRbacService(com.odong.core.service.RbacService rbacService) {
        this.rbacService = rbacService;
    }
}
