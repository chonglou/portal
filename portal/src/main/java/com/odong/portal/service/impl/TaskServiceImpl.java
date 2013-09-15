package com.odong.portal.service.impl;

import com.odong.portal.dao.TaskDao;
import com.odong.portal.entity.Task;
import com.odong.portal.model.task.ClockRequest;
import com.odong.portal.model.task.Request;
import com.odong.portal.service.TaskService;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午4:37
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {


    @Override
    public void setTaskRequest(String id, Request request) {
        Task task = taskDao.select(id);
        task.setRequest(jsonHelper.object2json(request));
        taskDao.update(task);
    }

    @Override
    public String addTask(Task.Type type, Request request, Date begin, Date end, Date nextRun) {
        String id = UUID.randomUUID().toString();
        Task task = new Task();
        task.setType(type);
        task.setId(id);
        task.setRequest(jsonHelper.object2json(request));
        task.setBegin(begin);
        task.setEnd(end);
        task.setNextRun(nextRun);
        task.setCrated(new Date());
        taskDao.insert(task);
        return id;
    }

    @Override
    public List<Task> listRunnableTask() {
        Map<String, Object> map = new HashMap<>();
        map.put("nextRun", new Date());
        return taskDao.list("FROM Task i WHERE i.nextRun<=:nextRun", map);
    }

    @Override
    public void setTaskNextRun(String task, Date nextRun) {
        Task t = taskDao.select(task);
        t.setIndex(t.getIndex() + 1);
        t.setNextRun(nextRun);
        taskDao.update(t);
    }

    @Override
    public void setTaskDone(String task) {
        Task t = taskDao.select(task);
        t.setNextRun(timeHelper.max());
        taskDao.update(t);
    }

    @Override
    public Request getTaskRequest(String id) {
        Task t = taskDao.select(id);
        switch (t.getType()) {
            case GC:
            case BACKUP:
            case RSS:
            case SITE_MAP:
                return jsonHelper.json2object(t.getRequest(), ClockRequest.class);
        }
        throw new IllegalArgumentException("未知的任务类型");  //
    }

    @Resource
    private TaskDao taskDao;
    @Resource
    private TimeHelper timeHelper;
    @Resource
    private JsonHelper jsonHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
}
