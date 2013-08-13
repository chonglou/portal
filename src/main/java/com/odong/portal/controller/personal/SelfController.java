package com.odong.portal.controller.personal;

import com.odong.portal.email.EmailHelper;
import com.odong.portal.entity.Log;
import com.odong.portal.entity.User;
import com.odong.portal.form.personal.*;
import com.odong.portal.model.Contact;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.RbacService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.TimeHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.PasswordField;
import com.odong.portal.web.form.TextAreaField;
import com.odong.portal.web.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller("c.personal.self")
@RequestMapping(value = "/personal/self")
@SessionAttributes(SessionItem.KEY)
public class SelfController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Form getInfo(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("info", "个人信息", "/personal/info");
        User u = accountService.getUser(si.getSsUserId());
        TextField<String> email = new TextField<>("email", "Email", u.getEmail());
        email.setReadonly(true);
        fm.addField(email);
        fm.addField(new TextField<>("username", "用户名", u.getUsername()));

        Contact c = jsonHelper.json2object(u.getContact(), Contact.class);
        if (c == null) {
            c = new Contact();
        }
        String[] ss = new String[]{
                "qq", "QQ号", c.getQq(),
                "tel", "电话", c.getTel(),
                "fax", "传真", c.getFax(),
                "address", "地址", c.getAddress(),
                "weixin", "微信", c.getWeixin(),
                "web", "个人站点", c.getWeb()
        };
        for (int i = 0; i < ss.length; i += 3) {
            TextField<String> tf = new TextField<>(ss[i], ss[i + 1], ss[i + 2]);
            tf.setRequired(false);
            fm.addField(tf);
        }
        TextAreaField taf = new TextAreaField("details", "详细信息", c.getDetails());
        taf.setHtml(true);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postInfo(@Valid ContactForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            Contact c = new Contact();
            c.setAddress(form.getAddress());
            c.setFax(form.getFax());
            c.setTel(form.getTel());
            c.setQq(form.getQq());
            c.setWeb(form.getWeb());
            c.setWeixin(form.getWeixin());
            c.setDetails(form.getDetails());
            accountService.setUserInfo(si.getSsUserId(), form.getUsername(), jsonHelper.object2json(c));
            si.setSsUsername(form.getUsername());
            ri.setType(ResponseItem.Type.redirect);
            ri.addData("/personal/self");
            logService.add(si.getSsUserId(), "更新个人信息", Log.Type.INFO);
        }
        return ri;

    }

    @RequestMapping(value = "/setPwd", method = RequestMethod.GET)
    @ResponseBody
    Form getSetPwd() {
        Form fm = new Form("setPwd", "设置密码", "/personal/setPwd");
        fm.addField(new PasswordField("oldPwd", "当前密码"));
        fm.addField(new PasswordField("newPwd", "新密码"));
        fm.addField(new PasswordField("rePwd", "再输一遍"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/setPwd", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSetPwd(@Valid SetPwdForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (!form.getNewPwd().equals(form.getRePwd())) {
            ri.setOk(false);
            ri.addData("两次密码输入不一致");
        }
        if (ri.isOk()) {

            if (accountService.auth(si.getSsEmail(), form.getOldPwd()) == null) {
                ri.setOk(false);
                ri.addData("当前密码输入有误");
            } else {
                accountService.setUserPassword(si.getSsUserId(), form.getNewPwd());
                emailHelper.send(si.getSsEmail(), "您在[" + siteService.getString("site.domain") + "]上的密码变更记录",
                        "如果不是您的操作，请忽略该邮件。", true);
            }
        }
        return ri;

    }


    @RequestMapping(value = "/log", method = RequestMethod.GET)
    String getLog(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("logList", logService.list(si.getSsUserId(), 100));
        return "personal/log";
    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private AccountService accountService;
    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private EmailHelper emailHelper;
    @Resource
    private JsonHelper jsonHelper;

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setEmailHelper(EmailHelper emailHelper) {
        this.emailHelper = emailHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

}
