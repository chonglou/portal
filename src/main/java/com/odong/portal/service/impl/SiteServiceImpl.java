package com.odong.portal.service.impl;

import com.odong.portal.service.SiteService;
import com.odong.portal.util.JsonHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:29
 */
@Service
public class SiteServiceImpl implements SiteService {
    @Override
    public String getEncryptKey() {
        return null;  //
    }


    @Override
    public void set(String key, Object value) {
        //
    }

    @Override
    public String get(String key) {
        return null;  //
    }

    @Resource
    private JsonHelper jsonHelper;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }
}
