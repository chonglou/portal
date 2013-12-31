package com.odong.core.job;

import com.odong.core.json.JsonHelper;
import com.odong.core.service.TaskService;
import com.odong.core.util.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by flamen on 13-12-30下午7:35.
 */
@Component("core.job.target")
public class TaskJob {
    public void execute() {
        taskService.listRunnableTask().forEach((t) -> {
            Date now = new Date();
            taskSender.send(t.getModule(),t.getType(), jsonHelper.json2map(t.getRequest(), String.class, Object.class));
            Date nextRun = timeHelper.plus(now, t.getSpace());
            if (nextRun.compareTo(t.getEnd()) < 0 && (t.getTotal() != 0 && t.getIndex() < t.getTotal())) {
                taskService.setTaskNextRun(t.getId(), nextRun);
            } else {
                taskService.setTaskNextRun(t.getId(), timeHelper.max());
            }
        });

    }

    @Resource
    private TaskSender taskSender;
    @Resource
    private TaskService taskService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TimeHelper timeHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskJob.class);

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

}
