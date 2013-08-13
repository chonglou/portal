package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.AllowForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.RadioField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:21
 */
@Controller("c.admin.state")
@RequestMapping(value = "/admin/state")
@SessionAttributes(SessionItem.KEY)
public class StateController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getAllow() {
        Form fm = new Form("allow", "站点权限编辑", "/admin/allow");
        fm.addField(new RadioField<>("allowRegister", "新用户注册", siteService.getBoolean("site.allowRegister")));
        fm.addField(new RadioField<>("allowLogin", "普通用户登录", siteService.getBoolean("site.allowLogin")));
        fm.setOk(true);
        return fm;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAllow(@Valid AllowForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.allowRegister", form.isAllowRegister());
            siteService.set("site.allowLogin", form.isAllowLogin());
            logService.add(si.getSsUserId(), "变更站点权限 注册=>[" + form.isAllowRegister() + "] 登陆=>[" + form.isAllowLogin() + "]", Log.Type.INFO);
        }
        return ri;

    }

    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public FormHelper getFormHelper() {
        return formHelper;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
