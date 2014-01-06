package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.entity.User;
import com.odong.core.service.LogService;
import com.odong.core.service.RbacService;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.core.util.TimeHelper;
import com.odong.platform.form.admin.ManagerForm;
import com.odong.platform.form.admin.UserForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.RadioField;
import com.odong.web.model.form.SelectField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("c.admin.user")
@RequestMapping(value = "/admin/user")
public class UserController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getList(Map<String, Object> map) {
        map.put("userList", userService.listUser());
        return "admin/user";
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postState(@Valid UserForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        check(ri, form.getId());
        if (ri.isOk()) {
            userService.setUserState(form.getId(), form.getState());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "变更用户[" + form.getId() + "]=>状态[" + form.getState() + "]", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/bind", method = RequestMethod.GET)
    @ResponseBody
    Form getBind() {
        Form fm = new Form("bind", "管理员权限管理", "/admin/user/bind");

        SelectField<Long> users = new SelectField<>("userId", "用户");
        for (User user : userService.listUser()) {
            users.addOption(user.toString(), user.getId());
        }
        users.setWidth(320);
        fm.addField(users);

        RadioField<Boolean> bind = new RadioField<>("bind", "操作", Boolean.FALSE);
        bind.addOption("绑定", Boolean.TRUE);
        bind.addOption("解绑", Boolean.FALSE);
        fm.addField(bind);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postBind(@Valid ManagerForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        check(ri, form.getUserId());
        if (ri.isOk()) {
            rbacService.bind("user://"+form.getUserId(), "manager", "resource://site",new Date(), timeHelper.max(), form.isBind());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), form.isBind() ? "绑定" : "解绑" + "用户[" + form.getUserId() + "]到管理员组", Log.Type.INFO);
        }
        return ri;
    }

    private void check(ResponseItem ri, long userId) {
        if (manager.equals(userService.getUser(userId).getEmail())) {
            ri.setOk(false);
            ri.addData("不能修改超级管理员");
        }
    }

    @Resource
    private RbacService rbacService;
    @Resource
    private UserService userService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private LogService logService;
    @Value("${app.manager}")
    private String manager;
    @Resource
    private TimeHelper timeHelper;

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
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
