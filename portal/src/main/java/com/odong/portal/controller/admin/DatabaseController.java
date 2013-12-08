package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.ImportForm;
import com.odong.portal.job.TaskSender;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.DBHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("c.admin.database")
@RequestMapping(value = "/admin/database")
@SessionAttributes(SessionItem.KEY)
public class DatabaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCompress(Map<String, Object> map) {

        map.put("lastExport", siteService.getObject("site.lastExport", Date.class));
        map.put("lastBackup", siteService.getObject("site.lastBackup", Date.class));
        map.put("dbSize", dbHelper.getSize());
        return "admin/database";
    }

    @RequestMapping(value = "/backup", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postBackup(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        try {
            taskSender.backup();
            siteService.set("site.lastBackup", new Date());
            logService.add(si.getSsUserId(), "备份数据库", Log.Type.INFO);
            ri.setOk(true);
        } catch (Exception e) {
            ri.setOk(false);
            ri.addData(e.getMessage());
        }
        return ri;

    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @ResponseBody
    Form getImport() {
        Form fm = new Form("database", "导入数据", "/admin/database/import");
        fm.addField(new TextField<String>("url", "路径"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postImport(@Valid ImportForm form, BindingResult result, HttpServletRequest request, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result, request, true);
        File file = new File(form.getUrl());
        if (!file.exists()) {
            ri.setOk(false);
            ri.addData("数据文件[" + form.getUrl() + "]不存在");
        }
        if (ri.isOk()) {
            taskSender.import4json(form.getUrl());
            ri.addData("正在处理，请稍候");
        }
        return ri;
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postExport(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        try {
            taskSender.export2json();
            siteService.set("site.lastExport", new Date());
            logService.add(si.getSsUserId(), "导出数据库", Log.Type.INFO);
            ri.setOk(true);
        } catch (Exception e) {
            ri.setOk(false);
            ri.addData(e.getMessage());
        }
        return ri;
    }

    @Resource
    private DBHelper dbHelper;
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
    private final static Logger logger = LoggerFactory.getLogger(DatabaseController.class);

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


    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
