package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.ScriptForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.HiddenField;
import com.odong.web.model.form.TextAreaField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-6
 * Time: 上午9:38
 */
@Controller("platform.c.admin.share")
@RequestMapping(value = "/admin/share")
public class ShareController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCode(Map<String, Object> map) {
        Map<String, String> codes = new HashMap<>();
        for (String s : new String[]{"qq", "qZone", "weiBo", "weiXin"}) {
            codes.put(s, siteService.get("site.share." + s, String.class));
        }
        map.put("codes", codes);
        return "/platform/admin/share";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getForm(@PathVariable String id) {
        Form fm = new Form("share", "编辑分享代码[" + id + "]", "/admin/share/");
        fm.addField(new HiddenField<>("id", id));
        TextAreaField taf = new TextAreaField("script", "代码", siteService.get("site.share." + id, String.class));
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSizeForm(@Valid ScriptForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.share." + form.getId(), form.getScript());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "更新分享代码", Log.Type.INFO);
            cacheService.popPage();
        }
        return ri;
    }


    @Resource
    private CacheService cacheService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
