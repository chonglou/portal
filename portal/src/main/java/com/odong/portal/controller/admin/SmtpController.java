package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.SmtpForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.model.SmtpProfile;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.RadioField;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-7-30
 * Time: 下午12:54
 */
@Controller("c.admin.smtp")
@RequestMapping(value = "/admin/smtp")
@SessionAttributes(SessionItem.KEY)
public class SmtpController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getSiteSmtp() {
        SmtpProfile profile = encryptHelper.decode(siteService.getString("site.smtp"), SmtpProfile.class);

        if (profile == null) {
            profile = new SmtpProfile();
            profile.setPort(25);
        }

        Form fm = new Form("siteSmtp", "SMTP信息", "/admin/smtp/");
        fm.addField(new TextField<>("host", "主机", profile.getHost()));
        fm.addField(new TextField<>("port", "端口", profile.getPort()));
        fm.addField(new TextField<>("username", "用户名", profile.getUsername()));
        fm.addField(new TextField<>("password", "密码"));
        RadioField<Boolean> ssl = new RadioField<>("ssl", "启用SSL", profile.isSsl());
        ssl.addOption("是", true);
        ssl.addOption("否", false);
        fm.addField(ssl);
        TextField<String> bcc = new TextField<>("bcc", "密送", profile.getBcc());
        bcc.setRequired(false);
        fm.addField(bcc);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSiteSmtp(@Valid SmtpForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            SmtpProfile profile = new SmtpProfile(form.getHost(), form.getUsername(), form.getPassword(), form.getBcc());
            profile.setPort(form.getPort());
            profile.setSsl(form.isSsl());
            siteService.set("site.smtp", encryptHelper.encode(profile));
            logService.add(si.getSsUserId(), "设置SMTP信息", Log.Type.INFO);
            cacheHelper.delete("site/smtp");
        }
        return ri;

    }

    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private LogService logService;
    @Resource
    private FormHelper formHelper;

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }


    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
