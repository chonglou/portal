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
 * Date: 13-8-14
 * Time: 下午3:48
 */
@Controller("platform.c.admin.advert")
@RequestMapping(value = "/admin/advert")
public class AdvertController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getAdvert(Map<String, Object> map) {
        Map<String, String> adverts = new HashMap<>();
        for (String s : new String[]{"left", "bottom"}) {
            adverts.put(s, siteService.get("site.advert." + s, String.class));
        }
        map.put("adverts", adverts);
        return "/platform/admin/advert";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getLeft(@PathVariable String id) {
        Form fm = new Form("advert", "编辑广告[" + id + "]", "/admin/advert/");
        fm.addField(new HiddenField<>("id", id));
        TextAreaField taf = new TextAreaField("script", "脚本", siteService.get("site.advert." + id, String.class));
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSiteSmtp(@Valid ScriptForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.advert." + form.getId(), form.getScript());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "设置广告[" + form.getId() + "]", Log.Type.INFO);
            cacheService.popPage();
        }
        return ri;
    }


    @Resource
    private FormHelper formHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private LogService logService;
    @Resource
    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
