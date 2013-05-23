package com.odong.portal.dao.impl;

import com.odong.portal.dao.SettingDao;
import com.odong.portal.entity.Setting;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午3:43
 */
@Repository("settingDao")
public class SettingDaoImpl extends BaseHibernate4DaoImpl<Setting,String> implements SettingDao {
    @Override
    public Setting get(String key) {
        Query query = getSession().createQuery("from  Setting as s where s.key=:id");
        query.setString("id", key);
        return (Setting)query.uniqueResult();
        //return (Setting)getSession().get(Setting.class, key);
    }
}
