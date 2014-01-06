package com.odong.platform.controller.personal;

import com.odong.core.entity.Log;
import com.odong.core.job.TaskSender;
import com.odong.core.service.LogService;
import com.odong.core.service.UserService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.personal.SetPwdForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.PasswordField;
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
 * Time: 上午1:15
 */
@Controller("c.personal.setPassword")
@RequestMapping(value = "/personal")
public class SetPasswordController {

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
    ResponseItem postSetPwd(@Valid SetPwdForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (!form.getNewPwd().equals(form.getRePwd())) {
            ri.setOk(false);
            ri.addData("两次密码输入不一致");
        }
        if (ri.isOk()) {

            SessionItem si = formHelper.getSessionItem(session);
            if (userService.auth(si.getSsEmail(), form.getOldPwd())) {
                userService.setUserPassword(si.getSsUserId(), form.getNewPwd());
                logService.add(si.getSsUserId(), "变更密码", Log.Type.INFO);
                taskSender.email(si.getSsEmail(), "您在[" + cacheService.getSiteTitle() + "]上的密码变更记录",
                        "如果不是您的操作，请忽略该邮件。", true, null);
            } else {
                ri.setOk(false);
                ri.addData("当前密码输入有误");
            }
        }
        return ri;

    }


    @Resource
    private CacheService cacheService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private UserService userService;
    @Resource
    private LogService logService;
    @Resource
    private TaskSender taskSender;

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }


}
