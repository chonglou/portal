package com.odong.portal.controller.personal;

import com.odong.portal.entity.User;
import com.odong.portal.form.personal.ResetPwdForm;
import com.odong.portal.job.TaskSender;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.PasswordField;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:21
 */
@Controller("c.personal.resetPassword")
@RequestMapping(value = "/personal")
@SessionAttributes(SessionItem.KEY)
public class ResetPasswordController {
    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postResetPwd(@Valid ResetPwdForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (!form.getNewPwd().equals(form.getRePwd())) {
            ri.setOk(false);
            ri.addData("两次密码输入不一致");
        }
        if (ri.isOk()) {
            User u = accountService.getUser(form.getEmail());
            if (u != null && u.getState() == User.State.ENABLE) {
                Map<String, Object> map = new HashMap<>();
                map.put("password", form.getNewPwd());
                taskSender.sendValidEmail(form.getEmail(), "reset_pwd", map);
                ri.addData("已向您的邮箱发送了密码重置链接，请进入邮箱进行操作。");

            } else {
                ri.addData("用户[" + form.getEmail() + "]状态不对");
                ri.setOk(false);
            }
        }
        return ri;

    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.GET)
    @ResponseBody
    Form getResetPwd() {
        Form fm = new Form("resetPwd", "找回密码", "/personal/resetPwd");
        fm.addField(new TextField("email", "邮箱"));
        fm.addField(new PasswordField("newPwd", "新密码"));
        fm.addField(new PasswordField("rePwd", "再次输入"));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private TaskSender taskSender;
    @Resource
    private AccountService accountService;

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
