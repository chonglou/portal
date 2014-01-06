package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.GoogleAuthForm;
import com.odong.platform.form.admin.QqAuthForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.RadioField;
import com.odong.web.model.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by flamen on 14-1-5下午10:59.
 */
@Controller("platform.c.admin.oauth")
@RequestMapping(value = "/admin/oauth")
public class OquthController {
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    @ResponseBody
    Form getGoogleAuthForm() {
        Form fm = new Form("google", "google账户", "/admin/oauth/google");
        GoogleAuthProfile gap = siteService.get("site.oauth.google", GoogleAuthProfile.class, true);
        if (gap == null) {
            gap = new GoogleAuthProfile("", "", "");
        }
        fm.addField(new TextField<>("id", "CLIENT ID", gap.getId()));
        fm.addField(new TextField<>("secret", "CLIENT SECRET", gap.getSecret()));
        fm.addField(new TextField<>("uri", "REDIRECT URI", gap.getUri()));
        RadioField<Boolean> enable = new RadioField<Boolean>("enable", "状态", gap.isEnable());
        enable.addOption("启用", Boolean.TRUE);
        enable.addOption("停用", Boolean.FALSE);
        fm.addField(enable);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postGoogleAuthForm(@Valid GoogleAuthForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            GoogleAuthProfile gap = new GoogleAuthProfile(form.getId(), form.getSecret(), form.getUri());
            gap.setEnable(form.isEnable());
            siteService.set("site.oauth.google", gap);
            cacheService.popGoogleAuthProfile();
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点QQ互联信息", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/qq", method = RequestMethod.GET)
    @ResponseBody
    Form getQqAuthForm() {
        Form fm = new Form("qq", "二维码信息", "/admin/oauth/qq");
        QqAuthProfile qap = siteService.get("site.oauth.qq", QqAuthProfile.class, true);
        if (qap == null) {
            qap = new QqAuthProfile("", "", "", "");
        }
        fm.addField(new TextField<>("valid", "验证代码", qap.getValid()));
        fm.addField(new TextField<>("id", "APP ID", qap.getId()));
        fm.addField(new TextField<>("key", "APP KEY", qap.getKey()));
        fm.addField(new TextField<>("uri", "回调路径", qap.getUri()));

        RadioField<Boolean> enable = new RadioField<Boolean>("enable", "状态", qap.isEnable());
        enable.addOption("启用", Boolean.TRUE);
        enable.addOption("停用", Boolean.FALSE);
        fm.addField(enable);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postQqAuthForm(@Valid QqAuthForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            QqAuthProfile qap = new QqAuthProfile(form.getValid(), form.getId(), form.getKey(), form.getUri());
            qap.setEnable(form.isEnable());
            siteService.set("site.oauth.qq", qap, true);
            cacheService.popQqAuthProfile();
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点QQ互联信息", Log.Type.INFO);
        }
        return ri;
    }


    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private CacheService cacheService;

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
