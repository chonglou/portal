package com.odong.portal.controller.personal;

import com.odong.portal.entity.Log;
import com.odong.portal.form.personal.SetPwdForm;
import com.odong.portal.job.TaskSender;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.PasswordField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午1:15
 */
@Controller("c.personal.setPassword")
@RequestMapping(value = "/personal")
@SessionAttributes(SessionItem.KEY)
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
                logService.add(si.getSsUserId(), "变更密码", Log.Type.INFO);
                taskSender.email(si.getSsEmail(), "您在[" + siteService.getString("site.domain") + "]上的密码变更记录",
                        "如果不是您的操作，请忽略该邮件。", true, null);
            }
        }
        return ri;

    }


    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private AccountService accountService;
    @Resource
    private LogService logService;
    @Resource
    private TaskSender taskSender;

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
}
