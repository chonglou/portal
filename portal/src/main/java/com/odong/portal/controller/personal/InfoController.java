package com.odong.portal.controller.personal;

import com.odong.portal.entity.Log;
import com.odong.portal.entity.User;
import com.odong.portal.form.personal.ContactForm;
import com.odong.portal.model.Contact;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.LogService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.util.JsonHelper;
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
 * Date: 13-9-11
 * Time: 上午1:21
 */
@Controller("c.personal.info")
@RequestMapping(value = "/personal")
@SessionAttributes(SessionItem.KEY)
public class InfoController {

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
            accountService.setUserName(si.getSsUserId(), form.getUsername());
            accountService.setUserContact(si.getSsUserId(), c);
            si.setSsUsername(form.getUsername());
            ri.setType(ResponseItem.Type.redirect);
            ri.addData("/personal/self");
            logService.add(si.getSsUserId(), "更新个人信息", Log.Type.INFO);
        }
        return ri;

    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private AccountService accountService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private LogService logService;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
