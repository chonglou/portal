package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.entity.Task;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午5:39
 */
@Controller("c.admin.task")
@RequestMapping(value = "/admin/task")
public class TaskController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getTask() {
        Form fm = new Form("task", "定时任务", "/admin/task/");
        fm.addField(getClockField("gc", "垃圾清理", getTaskClock(Task.Type.GC)));
        fm.addField(getClockField("rss", "RSS", getTaskClock(Task.Type.RSS)));
        fm.addField(getClockField("sitemap", "SITEMAP", getTaskClock(Task.Type.SITE_MAP)));
        fm.addField(getClockField("backup", "自动备份", getTaskClock(Task.Type.BACKUP)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postTask(@Valid TaskForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            setTaskClock(Task.Type.GC, form.getGc());
            setTaskClock(Task.Type.RSS, form.getRss());
            setTaskClock(Task.Type.SITE_MAP, form.getSitemap());
            setTaskClock(Task.Type.BACKUP, form.getBackup());
            logService.add(si.getSsUserId(), "更新定时任务" + jsonHelper.object2json(form), Log.Type.INFO);
        }
        return ri;

    }

    private int getTaskClock(Task.Type type) {
        return ((ClockRequest) taskService.getTaskRequest(siteService.getString("task." + type))).getClock();
    }

    private void setTaskClock(Task.Type type, int clock) {
        String taskId = siteService.getString("task." + type);
        taskService.setTaskRequest(taskId, new ClockRequest(clock));
        taskService.setTaskNextRun(taskId, timeHelper.nextDay(clock));
    }

    private SelectField<Integer> getClockField(String id, String name, int value) {
        SelectField<Integer> clock = new SelectField<Integer>(id, name, value);
        for (int i = 1; i <= 24; i++) {
            clock.addOption(String.format("%2d:00", i), i);
        }
        return clock;
    }

    @Resource
    private TaskService taskService;
    @Resource
    private SiteService siteService;
    @Resource
    private LogService logService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TimeHelper timeHelper;

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}