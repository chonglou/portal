package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.model.SmtpProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.SmtpForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.RadioField;
import com.odong.web.model.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-7-30
 * Time: 下午12:54
 */
@Controller("c.admin.smtp")
@RequestMapping(value = "/admin/smtp")
public class SmtpController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getSiteSmtp() {
        SmtpProfile profile = siteService.get("site.smtp", SmtpProfile.class, true);

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
    ResponseItem postSiteSmtp(@Valid SmtpForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            SmtpProfile profile = new SmtpProfile(form.getHost(), form.getUsername(), form.getPassword(), form.getBcc());
            profile.setPort(form.getPort());
            profile.setSsl(form.isSsl());
            siteService.set("site.smtp", profile, true);
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "设置SMTP信息", Log.Type.INFO);
            cacheService.popSmtp();
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

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
