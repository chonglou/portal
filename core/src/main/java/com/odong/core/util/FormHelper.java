package com.odong.core.util;

import com.odong.web.model.Page;
import com.odong.web.model.SessionItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by flamen on 14-1-2下午8:47.
 */
@Component("core.formHelper")
public class FormHelper  {
    public Page getPage(HttpSession session){
        Page page = cacheService.getPage();
        page.setSessionId(session.getId());
        SessionItem si = (SessionItem)session.getAttribute(SessionItem.KEY);
        if(si != null){
            page.setLogin(true);
        }
        return page;
    }

    @Resource
    protected CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
