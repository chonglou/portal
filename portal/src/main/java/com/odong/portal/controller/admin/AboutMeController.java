package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.AboutMeForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.util.CacheService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextAreaField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午12:38
 */
@Controller("c.admin.aboutMe")
@RequestMapping(value = "/admin/aboutMe")
@SessionAttributes(SessionItem.KEY)
public class AboutMeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getAboutMe() {
        Form fm = new Form("aboutMe", "设置关于我们", "/admin/aboutMe/");
        fm.addField(new TextAreaField("content", "内容", siteService.getString("site.aboutMe")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAboutMe(@Valid AboutMeForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.aboutMe", form.getContent());
            logService.add(si.getSsUserId(), "设置关于我们", Log.Type.INFO);
            cacheService.popAboutMe();
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
