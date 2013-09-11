package com.odong.server.controller.admin;

import com.odong.server.entity.Log;
import com.odong.server.model.SessionItem;
import com.odong.server.service.LogService;
import com.odong.server.service.SiteService;
import com.odong.server.util.DBHelper;
import com.odong.server.web.ResponseItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
            dbHelper.backup();
            siteService.set("site.lastBackup", new Date());
            logService.add(si.getSsUserId(), "备份数据库", Log.Type.INFO);
            ri.setOk(true);
        } catch (Exception e) {
            ri.setOk(false);
            ri.addData(e.getMessage());
        }
        return ri;

    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postCompress(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        try {
            dbHelper.export();
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
