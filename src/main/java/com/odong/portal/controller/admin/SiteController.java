package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.InfoForm;
import com.odong.portal.form.admin.RegProtocolForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextAreaField;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("c.admin.site")
@RequestMapping(value = "/admin/site")
@SessionAttributes(SessionItem.KEY)
public class SiteController {
    @RequestMapping(value = "/regProtocol", method = RequestMethod.GET)
    @ResponseBody
    Form getRegProtocolForm() {
        Form fm = new Form("regProtocol", "用户注册协议", "/admin/site/regProtocol");
        fm.addField(new TextField<>("protocol", siteService.getString("site.regProtocol")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/regProtocol", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegProtocolForm(@Valid RegProtocolForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.regProtocol", form.getProtocol());
            logService.add(si.getSsUserId(), "修改用户注册协议", Log.Type.INFO);
        }
        return ri;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Form getInfoForm() {
        Form fm = new Form("info", "站点信息编辑", "/admin/site/info");
        fm.addField(new TextField<>("title", "名称", siteService.getString("site.title")));
        fm.addField(new TextField<>("keywords", "关键字", siteService.getString("site.keywords"), "用空格隔开"));
        TextAreaField desc = new TextAreaField("description", "说明", siteService.getString("site.description"));
        desc.setHtml(false);
        fm.addField(desc);
        fm.addField(new TextField<>("copyright", "版权", siteService.getString("site.copyright")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postInfoForm(@Valid InfoForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
            logService.add(si.getSsUserId(), "修改站点基本信息", Log.Type.INFO);
        }
        return ri;
    }


    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
