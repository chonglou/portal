package com.odong.platform.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.ScriptForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.HiddenField;
import com.odong.portal.web.form.TextAreaField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-6
 * Time: 上午9:38
 */
@Controller("c.admin.share")
@RequestMapping(value = "/admin/share")
@SessionAttributes(SessionItem.KEY)
public class ShareController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCode(Map<String, Object> map) {
        Map<String, String> codes = new HashMap<>();
        for (String s : new String[]{"qq", "qZone", "weiBo", "weiXin"}) {
            codes.put(s, siteService.getString("site.share." + s));
        }
        map.put("codes", codes);
        cacheService.popShareCodes();
        return "admin/share";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getForm(@PathVariable String id) {
        Form fm = new Form("share", "编辑分享代码[" + id + "]", "/admin/share/");
        fm.addField(new HiddenField<>("id", id));
        TextAreaField taf = new TextAreaField("script", "代码", siteService.getString("site.share." + id));
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSizeForm(@Valid ScriptForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.share." + form.getId(), form.getScript());
            logService.add(si.getSsUserId(), "更新分享代码", Log.Type.INFO);
            cacheService.popShareCodes();
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
