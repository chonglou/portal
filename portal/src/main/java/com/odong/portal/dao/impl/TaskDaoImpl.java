package com.odong.portal.dao.impl;

import com.odong.portal.dao.TaskDao;
import com.odong.portal.entity.Task;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午4:40
 */
@Repository("taskDao")
public class TaskDaoImpl extends BaseJpa2DaoImpl<Task, String> implements TaskDao {
}
