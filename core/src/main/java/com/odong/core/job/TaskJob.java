package com.odong.core.job;

import com.odong.core.json.JsonHelper;
import com.odong.core.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-30下午7:35.
 */
@Component("core.job.target")
public class TaskJob {
    public void execute() {
        taskService.listRunnableTask().forEach((t) -> {
            taskSender.send(t.getModule(), t.getType(), jsonHelper.json2map(t.getRequest(), String.class, Object.class));
            taskService.setTaskNextRun(t.getId());
        });

    }

    @Resource
    private TaskSender taskSender;
    @Resource
    private TaskService taskService;
    @Resource
    private JsonHelper jsonHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskJob.class);

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }


    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

}
