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
 * Date: 13-8-14
 * Time: 下午3:48
 */
@Controller("c.admin.advert")
@RequestMapping(value = "/admin/advert")
@SessionAttributes(SessionItem.KEY)
public class AdvertController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getAdvert(Map<String, Object> map) {
        Map<String, String> adverts = new HashMap<>();
        for (String s : new String[]{"left", "bottom"}) {
            adverts.put(s, siteService.getString("site.advert." + s));
        }
        map.put("adverts", adverts);
        return "admin/advert";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getLeft(@PathVariable String id) {
        Form fm = new Form("advert", "编辑广告[" + id + "]", "/admin/advert/");
        fm.addField(new HiddenField<>("id", id));
        TextAreaField taf = new TextAreaField("script", "脚本", siteService.getString("site.advert." + id));
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSiteSmtp(@Valid ScriptForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.advert." + form.getId(), form.getScript());
            logService.add(si.getSsUserId(), "设置广告[" + form.getId() + "]", Log.Type.INFO);
            cacheService.popSiteInfo();
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
