package com.odong.portal.job;

import com.odong.portal.model.task.ClockRequest;
import com.odong.portal.service.TaskService;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-4
 * Time: 下午12:46
 */
@Component("taskJobTarget")
public class TaskJob {
    public void execute() {
        taskService.listRunnableTask().forEach((t) -> {
            Date nextRun = null;
            switch (t.getType()) {
                case GC:
                    nextRun = nextClock(t.getRequest());
                    taskSender.gc();
                    break;
                case RSS:
                    nextRun = nextClock(t.getRequest());
                    taskSender.rss();
                    break;
                case SITE_MAP:
                    nextRun = nextClock(t.getRequest());
                    taskSender.sitemap();
                    break;
                case BACKUP:
                    nextRun = nextClock(t.getRequest());
                    taskSender.backup();
                    break;
                default:
                    logger.info("未知的任务类型[{}-{}]", t.getId(), t.getType());
                    break;
            }
            if (nextRun != null) {
                taskService.setTaskNextRun(t.getId(), nextRun);
            }
        });
    }

    private Date nextClock(String request) {
        return timeHelper.nextDay(jsonHelper.json2object(request, ClockRequest.class).getClock());
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
