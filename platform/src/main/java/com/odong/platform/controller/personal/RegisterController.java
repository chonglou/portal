package com.odong.platform.controller.personal;

import com.odong.core.entity.User;
import com.odong.core.job.TaskSender;
import com.odong.core.service.SiteService;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.personal.ActiveForm;
import com.odong.platform.form.personal.RegisterForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.AgreeField;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.PasswordField;
import com.odong.web.model.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:21
 */
@Controller("c.personal.register")
@RequestMapping(value = "/personal")
public class RegisterController {
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    @ResponseBody
    Form getActive() {
        Form fm = new Form("active", "激活账户", "/personal/active");
        fm.addField(new TextField("email", "邮箱"));
        fm.addField(new AgreeField("agree", "用户协议", siteService.get("site.regProtocol", String.class)));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postActive(@Valid ActiveForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (!form.isAgree()) {
            ri.setOk(false);
            ri.addData("您需要同意用户协议才能继续");
        }
        if (ri.isOk()) {
            User u = userService.getUser(form.getEmail(), User.Type.EMAIL);
            if (u != null && u.getState() == User.State.SUBMIT) {
                taskSender.valid(form.getEmail(), "register", "激活账户", "激活账户", new HashMap<String, Object>());
                ri.addData("已向您的邮箱发送了账户激活链接，请进入邮箱进行操作。");
            } else {
                ri.setOk(false);
                ri.addData("邮箱[" + form.getEmail() + "]状态不对");
            }
        }
        return ri;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    Form getRegister() {
        Form fm = new Form("register", "注册账户", "/personal/register");
        fm.addField(new TextField("email", "邮箱"));
        fm.addField(new TextField("username", "用户名"));
        fm.addField(new PasswordField("newPwd", "登陆密码"));
        fm.addField(new PasswordField("rePwd", "再次输入"));
        fm.addField(new AgreeField("agree", "用户协议", siteService.get("site.regProtocol", String.class)));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegister(@Valid RegisterForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (!form.getNewPwd().equals(form.getRePwd())) {
            ri.setOk(false);
            ri.addData("两次密码输入不一致");
        }
        if (!siteService.get("site.allowRegister", Boolean.class)) {
            ri.setOk(false);
            ri.addData("站点禁止注册新账户");
        }
        if (!form.isAgree()) {
            ri.setOk(false);
            ri.addData("您需要同意用户协议才能继续");
        }
        if (ri.isOk()) {
            User u = userService.getUser(form.getEmail(), User.Type.EMAIL);
            if (u == null) {
                userService.addEmailUser(form.getEmail(), form.getUsername(), form.getNewPwd());
                taskSender.valid(form.getEmail(), "register", "账户注册", "激活账户", new HashMap<String, Object>());
                ri.addData("已向您的邮箱发送一封激活邮件，请进入邮箱继续操作.");
            } else {
                ri.setOk(false);
                ri.addData("邮箱[" + form.getEmail() + "]已存在");
            }
        }
        return ri;
    }

    @Resource
    private UserService userService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private TaskSender taskSender;

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

}
