package com.odong.core.service;

import com.odong.core.entity.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-30下午7:36.
 */
public interface TaskService {
    void setTaskRequest(long task, String request);

    void addTask(String module, String type, String request, Date begin, int space, int total);

    void addTask(String module, String type, String request, int clock);

    void addTask(String module, String type, String request, Date begin, Date end, int space);

    String getTaskRequest(long task);

    List<Task> listRunnableTask();

    void setTaskNextRun(long task);

    Task getTask(long task);
}
