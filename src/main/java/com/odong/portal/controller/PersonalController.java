package com.odong.portal.controller;

import com.odong.portal.entity.User;
import com.odong.portal.form.personal.LoginForm;
import com.odong.portal.form.personal.RegisterForm;
import com.odong.portal.form.personal.ResetPwdForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.*;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.PasswordField;
import com.odong.portal.web.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller
@RequestMapping(value = "/personal")
public class PersonalController {

        @RequestMapping(value = "/reset_pwd/active", method = RequestMethod.GET)
        @ResponseBody
        ResponseItem getResetPwdActive(@RequestParam("code") String code){
            ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
            try{
                Map<String,String> map = jsonHelper.json2map(encryptHelper.decode(code), String.class, String.class);

                if(timeHelper.plus(new Date(), -30*60).getTime() > Long.parseLong(map.get("created"))){
                    ri.addMessage("找回密码链接失效");
                }
                else {
                    accountService.setUserPassword(Long.parseLong(map.get("userId")), map.get("password"));
                    ri.setOk(true);
                }
            }
            catch (Exception e){
                ri.addMessage("找回密码失败");
            }
            return ri;
        }

    @RequestMapping(value = "/reset_pwd", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postResetPwd(@Valid ResetPwdForm form, BindingResult result, HttpServletRequest request){
        ResponseItem ri = formHelper.check(result, request, true);

        if(ri.isOk()){
            if(form.getNewPassword().equals(form.getRePassword())){
            User user = accountService.getUser(form.getEmail());
            if(user == null){
                ri.setOk(false);
                ri.addMessage("账户"+form.getEmail()+"]不存在");
            }
            else {
                Map<String, String> map = new HashMap<>();
                map.put("userId", Long.toString(user.getId()));
                map.put("password", form.getNewPassword());
                map.put("created", Long.toString(new Date().getTime()));

                String code = encryptHelper.encode(jsonHelper.object2json(map));
                String title = siteService.getString("site.title");
                String domain = siteService.getString("site.domain");

                emailHelper.send(form.getEmail(), "重置密码-"+title,
                        "亲爱的"+form.getEmail()+
                                "<br> &nbsp;&nbsp; 您好，<a href='http://"+domain+"/personal/reset_pwd/active?code="+code+"' target='_blank'>请点击此链接重置密码</a>，三十分钟内有效。"+
                                "<p>&nbsp;</p> &nbsp; 谢谢"
                        ,true);
            }
            }
            else {
                ri.setOk(false);
                ri.addMessage("两次密码输入不一致");
            }
        }
        return ri;
    }

    @RequestMapping(value = "/reset_pwd", method = RequestMethod.GET)
    @ResponseBody
    Form getResetPwd() {
        Form fm = new Form("resetPwd", "重置密码", "/personal/reset_pwd");
        fm.addField(new TextField<String>("email", "邮箱地址"));
        fm.addField(new PasswordField("newPassword", "新密码"));
        fm.addField(new PasswordField("rePassword", "再次输入"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    Form getRegister() {
        Form fm = new Form("register", "欢迎注册", "/personal/register");
        fm.addField(new TextField<String>("username", "用户名"));
        fm.addField(new TextField<String>("email", "邮箱地址"));
        fm.addField(new PasswordField("password", "登录密码"));
        fm.addField(new PasswordField("rePassword", "再次输入"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/register/active", method = RequestMethod.GET)
    @ResponseBody
    ResponseItem getActive(@RequestParam("code") String code) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        try {
            Map<String,String> map  = jsonHelper.json2map(encryptHelper.decode(code), String.class, String.class);
            String email = map.get("email");
            User user = accountService.getUser(email);

            if(user == null){
                if(timeHelper.plus(new Date(), -30*60).getTime() > Long.parseLong(map.get("created"))){
                    ri.addMessage("注册链接超时[30分钟]失效，请重新注册");
                }
                else {
                    accountService.addUser(map.get("email"), map.get("username"), map.get("password"));
                    ri.addMessage("/");
                    ri.setType(ResponseItem.Type.redirect);
                    ri.setOk(true);
                }
            }
            else {
                ri.addMessage("邮箱["+map.get("email")+"]已存在");
            }



        } catch (Exception e) {
            logger.error("激活账户", e);
            ri.addMessage("激活账户失败");
        }
        return ri;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegister(@Valid RegisterForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (ri.isOk()) {
            User user = accountService.getUser(form.getEmail());
            if (user == null) {

                Map<String,String> map = new HashMap<>();
                map.put("email", form.getEmail());
                map.put("username", form.getUsername());
                map.put("password", form.getPassword());
                map.put("created", Long.toString(new Date().getTime()));
                String code = encryptHelper.encode(jsonHelper.object2json(map));

                String domain = siteService.getString("site.domain");
                String title = siteService.getString("site.title");
                emailHelper.send(
                        form.getEmail(),
                        "来自[" + title + "]的注册邮件",
                        "亲爱的" + form.getEmail() +
                                ":<br> &nbsp; &nbsp; 你好，欢迎注册<a href='http://" + domain + "' target='_blank'>" + title + "</a>" +
                                "<br> &nbap; &nbap; <a href='http://" + domain + "/personal/active?code=" + code + "'>请点击此链接以激活账户</a>，30分钟内有效。" +
                                "<p>&nbsp;</p> &nbsp; 谢谢"
                        , true);

                ri.addMessage("已经发送注册邮件，请打开邮箱点击链接激活账户。");
            } else {

                    ri.setOk(false);
                    ri.addMessage("邮箱[" + form.getEmail() + "]已存在");

            }
        }
        ri.setType(ResponseItem.Type.message);
        return ri;

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    Form getLogin() {
        Form fm = new Form("login", "欢迎登录", "/personal/login");
        fm.addField(new TextField<String>("email", "邮箱"));
        fm.addField(new PasswordField("password", "密码"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postLogin(@Valid LoginForm form, BindingResult result, HttpServletRequest request, HttpSession session) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (ri.isOk()) {
            User user = accountService.auth(form.getEmail(), form.getPassword());
            if (user == null) {
                ri.setOk(false);
                ri.addMessage("邮箱与密码不匹配");
                ri.setType(ResponseItem.Type.message);
            } else if (user.getState() == User.State.LOCK) {
                ri.setOk(false);
                ri.addMessage("账户被锁定，请联系管理员解决");
                ri.setType(ResponseItem.Type.message);
            } else if (user.getState() == User.State.DISABLE) {
                ri.setOk(false);
                ri.addMessage("账户被禁用");
                ri.setType(ResponseItem.Type.message);
            } else {
                SessionItem si = new SessionItem(user.getId(), user.getUsername(), user.getEmail());
                session.setAttribute(SessionItem.KEY, si);
                ri.addMessage("/personal/self");
                ri.setType(ResponseItem.Type.redirect);
            }
        }

        return ri;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem logout(HttpSession session) {
        session.invalidate();
        ResponseItem ri = new ResponseItem(ResponseItem.Type.redirect);
        ri.addMessage("/");
        ri.setOk(true);
        return ri;
    }

    @Resource
    private TimeHelper timeHelper;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private StringHelper stringHelper;
    @Resource
    private EmailHelper emailHelper;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private FormHelper formHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private AccountService accountService;
    private final static Logger logger = LoggerFactory.getLogger(PersonalController.class);

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setEmailHelper(EmailHelper emailHelper) {
        this.emailHelper = emailHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
