package com.odong.portal.service.impl;

import com.odong.portal.dao.LogDao;
import com.odong.portal.entity.Log;
import com.odong.portal.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:38
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Override
    public void add(Long user, String message, Log.Type type) {
        Log log = new Log();
        log.setUser(user);
        log.setMessage(message);
        log.setType(type);
        log.setCreated(new Date());
        logDao.insert(log);
    }

    @Override
    public List<Log> list(int no, int size) {
        return logDao.list(no, size, logDao.hqlListAll(), null);
    }

    @Override
    public List<Log> list(Long user) {
        Map<String, Object> map = new HashMap<>();
        return logDao.list("FROM User AS i WHERE i.user=:user", map);
    }

    @Override
    public void delete(Long id) {
        logDao.delete(id);
    }

    @Resource
    private LogDao logDao;

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

}
