package com.odong.portal.dao.impl;

import com.odong.portal.dao.LogDao;
import com.odong.portal.entity.Log;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:34
 */
@Repository("logDao")
public class LogDaoImpl extends BaseJpa2DaoImpl<Log, Long> implements LogDao {
}
