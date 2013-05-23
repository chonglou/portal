package com.odong.portal.service.impl;

import com.odong.portal.dao.SettingDao;
import com.odong.portal.entity.Setting;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void setObject(String key, Object value) {
        Setting s = settingDao.select(key);
        String val = jsonHelper.object2json(value);
        if (s == null) {
            s = new Setting();
            s.setKey(key);
            s.setValue(val);
            settingDao.insert(s);
        } else {
            s.setValue(val);
            settingDao.update(s);
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        Setting s = settingDao.select(key);
        if (s == null) {
            return null;
        }
        return jsonHelper.json2object(s.getValue(), clazz);  //
    }

    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private SettingDao settingDao;
    private Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    public void setSettingDao(SettingDao settingDao) {
        this.settingDao = settingDao;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }
}
