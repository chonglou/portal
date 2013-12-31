package com.odong.core.service;

import com.odong.core.entity.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-30下午7:36.
 */
public interface TaskService {
    List<Task> listRunnableTask();

    void setTaskNextRun(long id, Date date);
}
