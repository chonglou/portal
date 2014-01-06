package com.odong.platform.controller.personal;

import com.odong.core.entity.Log;
import com.odong.core.entity.User;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.Contact;
import com.odong.core.service.LogService;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.personal.ContactForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.TextAreaField;
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
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午1:21
 */
@Controller("c.personal.info")
@RequestMapping(value = "/personal")
public class InfoController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Form getInfo(HttpSession session) {
        Form fm = new Form("info", "个人信息", "/personal/info");
        User u = userService.getUser(formHelper.getSessionItem(session).getSsUserId());
        TextField<String> email = new TextField<>("email", "Email", u.getEmail());
        email.setReadonly(true);
        fm.addField(email);
        fm.addField(new TextField<>("username", "用户名", u.getUsername()));

        Contact c = jsonHelper.json2object(userService.getUserContact(u.getId()), Contact.class);
        if (c == null) {
            c = new Contact();
        }
        String[] ss = new String[]{
                "qq", "QQ号", c.getQq(),
                "tel", "电话", c.getTel(),
                "fax", "传真", c.getFax(),
                "address", "地址", c.getAddress(),
                "weChat", "微信", c.getWeChat(),
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
    ResponseItem postInfo(@Valid ContactForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            SessionItem si = formHelper.getSessionItem(session);
            Contact c = new Contact();
            c.setAddress(form.getAddress());
            c.setFax(form.getFax());
            c.setTel(form.getTel());
            c.setQq(form.getQq());
            c.setWeb(form.getWeb());
            c.setWeChat(form.getWeChat());
            c.setDetails(form.getDetails());
            userService.setUserName(si.getSsUserId(), form.getUsername());
            userService.setUserContact(si.getSsUserId(), c);
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
    private UserService userService;
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
