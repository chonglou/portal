package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.job.TaskSender;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.store.DbUtil;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.ImportForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("platform.c.admin.database")
@RequestMapping(value = "/admin/database")
public class DatabaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCompress(Map<String, Object> map) {

        map.put("lastExport", siteService.get("site.lastExport", Date.class));
        map.put("lastBackup", siteService.get("site.lastBackup", Date.class));
        map.put("dbSize", dbUtil.size());
        return "admin/database";
    }

    @RequestMapping(value = "/backup", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postBackup(HttpSession session) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        try {
            taskSender.send(null, "backup", null);
            siteService.set("site.lastBackup", new Date());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "备份数据库", Log.Type.INFO);
            ri.setOk(true);
        } catch (Exception e) {
            ri.setOk(false);
            ri.addData(e.getMessage());
        }
        return ri;

    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @ResponseBody
    Form
    getImport() {
        Form fm = new Form("database", "导入数据", "/admin/database/import");
        fm.addField(new TextField<String>("url", "路径"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postImport(@Valid ImportForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        File file = new File(form.getUrl());
        if (!file.exists()) {
            ri.setOk(false);
            ri.addData("数据文件[" + form.getUrl() + "]不存在");
        }
        if (ri.isOk()) {
            Map<String,Object> map = new HashMap<>();
            map.put("url", form.getUrl());
            taskSender.send(null, "import4json", map);
            ri.addData("正在处理，请稍候");
        }
        return ri;
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postExport(HttpSession session) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        try {
            taskSender.send(null, "export4json", null);
            siteService.set("site.lastExport", new Date());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "导出数据库", Log.Type.INFO);
            ri.setOk(true);
        } catch (Exception e) {
            ri.setOk(false);
            ri.addData(e.getMessage());
        }
        return ri;
    }

    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Value("${app.store}")
    private String appStoreDir;
    @Resource
    private FormHelper formHelper;
    @Resource
    private TaskSender taskSender;
    @Resource
    private DbUtil dbUtil;
    private final static Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }



    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
