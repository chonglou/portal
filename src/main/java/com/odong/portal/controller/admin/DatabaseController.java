package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.DBHelper;
import com.odong.portal.web.ResponseItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
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
    Map<String, Object> postCompress(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        Map<String, Object> map = new HashMap<>();
        map.put("userList", accountService.listUser());
        map.put("articleList", contentService.listArticle());
        map.put("tagList", contentService.listTag());
        map.put("commentList", contentService.listComment());
        map.put("articleTagList", contentService.listArticleTag());
        map.put("friendLinkList", siteService.listFriendLink());
        Date now = new Date();
        map.put("created", now);
        siteService.set("site.lastExport", now);
        logService.add(si.getSsUserId(), "导出数据[" + now + "]", Log.Type.INFO);
        return map;
    }

    @Resource
    private DBHelper dbHelper;
    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private AccountService accountService;
    @Resource
    private ContentService contentService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
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
