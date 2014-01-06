package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.AboutMeForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.TextAreaField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午12:38
 */
@Controller("c.admin.aboutMe")
@RequestMapping(value = "/admin/aboutMe")
public class AboutMeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getAboutMe() {
        Form fm = new Form("aboutMe", "设置关于我们", "/admin/aboutMe/");
        fm.addField(new TextAreaField("content", "内容", siteService.get("site.aboutMe", String.class)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAboutMe(@Valid AboutMeForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.aboutMe", form.getContent());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "设置关于我们", Log.Type.INFO);
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
