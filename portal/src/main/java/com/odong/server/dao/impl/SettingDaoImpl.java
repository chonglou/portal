package com.odong.server.dao.impl;

import com.odong.server.dao.SettingDao;
import com.odong.server.entity.Setting;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午3:43
 */
@Repository("settingDao")
public class SettingDaoImpl extends BaseJpa2DaoImpl<Setting, String> implements SettingDao {

}
