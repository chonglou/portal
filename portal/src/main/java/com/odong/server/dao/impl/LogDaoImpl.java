package com.odong.server.dao.impl;

import com.odong.server.dao.LogDao;
import com.odong.server.entity.Log;
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
