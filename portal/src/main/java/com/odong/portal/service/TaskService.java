package com.odong.portal.service;

import com.odong.portal.entity.Task;
import com.odong.portal.model.task.Request;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午4:37
 */
public interface TaskService {
    void setTaskRequest(String task, Request request);

    String addTask(Task.Type type, Request request, Date begin, Date end, Date nextRun);

    List<Task> listRunnableTask();

    void setTaskNextRun(String task, Date nextRun);

    void setTaskDone(String task);

    Request getTaskRequest(String id);
}
