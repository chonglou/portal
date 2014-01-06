package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.model.KaptchaProfile;
import com.odong.core.model.ReCaptchaProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.CaptchaForm;
import com.odong.platform.util.CaptchaHelper;
import com.odong.web.model.ResponseItem;
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
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller("platform.c.admin.captcha")
@RequestMapping(value = "/admin/captcha")
public class CaptchaController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCaptcha() {
        return "admin/captcha";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Form getCaptchaForm() {
        KaptchaProfile kp = siteService.get("site.kaptcha", KaptchaProfile.class);
        ReCaptchaProfile rp = siteService.get("site.reCaptcha", ReCaptchaProfile.class, true);
        Form fm = new Form("captcha", "验证码设置", "/admin/captcha/info");
        RadioField<String> mode = new RadioField<>("mode", "引擎", siteService.get("site.captcha", String.class));
        mode.addOption("内置(随机图片)", "kaptcha");
        mode.addOption("外部(reCaptcha)", "reCaptcha");
        fm.addField(mode);

        fm.addField(new TextField<>("length", "长度", kp.getLength()));
        fm.addField(new TextField<>("width", "宽度", kp.getWidth()));
        fm.addField(new TextField<>("height", "高度", kp.getHeight()));
        fm.addField(new TextField<>("chars", "字符串", kp.getChars()));
        fm.addField(new TextField<>("publicKey", "公钥", rp.getPublicKey()));
        fm.addField(new TextField<>("privateKey", "私钥", rp.getPrivateKey()));

        RadioField<Boolean> rf = new RadioField<>("includeNoScript", "禁止脚本", rp.isIncludeNoScript());
        rf.addOption("是", true);
        rf.addOption("否", false);
        fm.addField(rf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postCaptchaForm(@Valid CaptchaForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            switch (form.getMode()) {
                case "kaptcha":
                    KaptchaProfile kp = new KaptchaProfile();
                    kp.setHeight(form.getHeight());
                    kp.setWidth(form.getWidth());
                    kp.setChars(form.getChars());
                    kp.setLength(form.getLength());
                    siteService.set("site.kaptcha", kp);
                    break;
                case "reCaptcha":
                    ReCaptchaProfile rp = new ReCaptchaProfile();
                    rp.setIncludeNoScript(form.isIncludeNoScript());
                    rp.setPublicKey(form.getPublicKey());
                    rp.setPrivateKey(form.getPrivateKey());
                    siteService.set("site.reCaptcha", rp, true);
                    break;
                default:
                    ri.setOk(false);
                    ri.addData("未知的验证码引擎");
                    break;
            }
        }
        if (ri.isOk()) {
            siteService.set("site.captcha", form.getMode());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "更新验证码配置", Log.Type.INFO);
            captchaHelper.reload();
        }
        return ri;
    }


    @Resource
    private CaptchaHelper captchaHelper;
    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;


    public void setCaptchaHelper(CaptchaHelper captchaHelper) {
        this.captchaHelper = captchaHelper;
    }

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
