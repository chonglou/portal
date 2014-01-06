package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.json.JsonHelper;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.service.TaskService;
import com.odong.core.util.FormHelper;
import com.odong.core.util.TimeHelper;
import com.odong.platform.form.admin.TaskForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.SelectField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午5:39
 */
@Controller("platform.c.admin.task")
@RequestMapping(value = "/admin/task")
public class TaskController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getTask() {
        Form fm = new Form("task", "定时任务", "/admin/task/");
        fm.addField(getClockField("gc", "垃圾清理", getTaskClock("gc")));
        fm.addField(getClockField("rss", "RSS", getTaskClock("rss")));
        fm.addField(getClockField("sitemap", "SITEMAP", getTaskClock("sitemap")));
        fm.addField(getClockField("backup", "自动备份", getTaskClock("backup")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postTask(@Valid TaskForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            setTaskClock("gc", form.getGc());
            setTaskClock("rss", form.getRss());
            setTaskClock("sitemap", form.getSitemap());
            setTaskClock("backup", form.getBackup());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "更新定时任务" + jsonHelper.object2json(form), Log.Type.INFO);
        }
        return ri;

    }

    private int getTaskClock(String type) {
        return timeHelper.getClock(taskService.getTask(siteService.get("task." + type, Long.class)).getNextRun());
    }

    private void setTaskClock(String type, int clock) {
        long taskId = Integer.parseInt(siteService.get("task." + type, String.class));
        taskService.setTaskRequest(taskId, null, clock);
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