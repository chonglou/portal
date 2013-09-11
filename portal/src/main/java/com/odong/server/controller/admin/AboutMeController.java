package com.odong.server.controller.admin;

import com.odong.server.entity.Log;
import com.odong.server.form.admin.AboutMeForm;
import com.odong.server.model.SessionItem;
import com.odong.server.service.LogService;
import com.odong.server.service.SiteService;
import com.odong.server.util.FormHelper;
import com.odong.server.web.ResponseItem;
import com.odong.server.web.form.Form;
import com.odong.server.web.form.TextAreaField;
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
    Form getAboutMe(){
        Form fm =new Form("aboutMe", "设置关于我们", "/admin/aboutMe/");
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
        }
        return ri;

    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private LogService logService;

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
