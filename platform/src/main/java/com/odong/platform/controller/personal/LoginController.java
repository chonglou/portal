package com.odong.platform.controller.personal;

import com.odong.core.entity.User;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.personal.LoginForm;
import com.odong.platform.util.CacheService;
import com.odong.platform.util.RbacService;
import com.odong.web.model.ResponseItem;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:36
 */
@Controller("c.personal.login")
@RequestMapping(value = "/personal")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    Form getLogin() {
        Form fm = new Form("login", "欢迎登录", "/personal/login");
        fm.addField(new TextField("email", "邮箱"));
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
            if (userService.auth(form.getEmail(), form.getPassword())) {
                User u = userService.getUser(form.getEmail(), User.Type.EMAIL);
                switch (u.getState()) {
                    case ENABLE:
                        boolean isAdmin = rbacService.isAdmin(u.getId());
                        if (cacheService.isSiteAllowLogin() || isAdmin) {
                            formHelper.login(session, User.Type.EMAIL, u.getId(), u.getUsername(), u.getLogo(), u.getEmail(), isAdmin);
                            ri.setType(ResponseItem.Type.redirect);
                            ri.addData("/personal/self");
                        } else {
                            ri.setOk(false);
                            ri.addData("站点禁止登陆");

                        }
                        break;
                    case DISABLE:
                        ri.setOk(false);
                        ri.addData("账户[" + form.getEmail() + "]被禁用");
                        break;
                    case SUBMIT:
                        ri.setOk(false);
                        ri.addData("账户[" + form.getEmail() + "]未激活");
                        break;
                    default:
                        ri.setOk(false);
                        ri.addData("未知错误");
                        break;
                }
            } else {
                ri.setOk(false);
                ri.addData("账户密码不匹配");
            }
        }
        return ri;
    }

    @Resource
    private RbacService rbacService;
    @Resource
    private CacheService cacheService;
    @Resource
    private UserService userService;
    @Resource
    private FormHelper formHelper;


    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }


    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }

}
