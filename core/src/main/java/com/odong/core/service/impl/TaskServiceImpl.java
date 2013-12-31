package com.odong.core.service.impl;

import com.odong.core.entity.Task;
import com.odong.core.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-30下午7:36.
 */
@Service("core.taskService")
public class TaskServiceImpl implements TaskService {
    @Override
    public List<Task> listRunnableTask() {
        return null;
    }

    @Override
    public void setTaskNextRun(long id, Date date) {

    }
}
