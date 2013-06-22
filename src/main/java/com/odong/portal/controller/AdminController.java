package com.odong.portal.controller;

import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.form.admin.AllowForm;
import com.odong.portal.form.admin.InfoForm;
import com.odong.portal.form.admin.ManagerForm;
import com.odong.portal.form.admin.TagForm;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.RbacService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @RequestMapping(value = "/site", method = RequestMethod.GET)
    @ResponseBody
    Form getTitleForm() {
        Form fm = new Form("title", "站点信息编辑", "/admin/title");
        fm.addField(new TextField<>("title", "名称", siteService.getString("site.title")));
        fm.addField(new TextField<>("keywords", "关键字", siteService.getString("site.keywords"), "用空格隔开"));
        fm.addField(new TextAreaField("description", "说明", siteService.getString("site.description")));
        fm.addField(new TextField<>("copyright", "版权", siteService.getString("site.copyright")));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/site", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postInfoForm(@Valid InfoForm form, BindingResult result) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
        }
        return ri;
    }


    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    @ResponseBody
    Form getManagerForm() {
        Form fm = new Form("manager", "站点管理员", "/admin/manager");
        SelectField<Long> users = new SelectField<>("userId", "用户");
        for (User u : accountService.listUser()) {
            users.addOption(u.getUsername() + "[" + u.getEmail() + "]", u.getId());
        }
        fm.addField(users);
        RadioField<Boolean> bind = new RadioField<>("bind", "绑定", false);
        bind.addOption("是", true);
        bind.addOption("否", false);
        fm.addField(bind);
        fm.setCaptcha(true);
        return fm;
    }


    @RequestMapping(value = "/manager", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postManager(@Valid ManagerForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (ri.isOk()) {
            rbacService.bindAdmin(form.getUserId(), form.isBind());
        }
        return ri;
    }


    @RequestMapping(value = "/allow", method = RequestMethod.GET)
    @ResponseBody
    Form getAllowForm() {
        Form fm = new Form("allow", "站点权限编辑", "/admin/allow");
        fm.addField(new RadioField<>("allowRegister", "新用户注册", siteService.getBoolean("site.allowRegister")));
        fm.addField(new RadioField<>("allowLogin", "普通用户登录", siteService.getBoolean("site.allowLogin")));
        fm.setCaptcha(true);
        fm.setOk(true);
        return fm;
    }


    @RequestMapping(value = "/allow", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAllowForm(@Valid AllowForm form, BindingResult result, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (ri.isOk()) {
            siteService.set("site.allowRegister", form.isAllowRegister());
            siteService.set("site.allowLogin", form.isAllowLogin());
        }
        return ri;

    }


    @RequestMapping(value = "/tag/list", method = RequestMethod.GET)
    @ResponseBody
    ResponseItem getTagList() {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        ri.addMessage(contentService.listTag());
        ri.setOk(true);
        return ri;
    }

    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    @ResponseBody
    Form getTagForm() {
        Form fm = new Form("tag", "标签", "/admin/tag");
        fm.addField(new TextField<>("name", "名称", null));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
    @ResponseBody
    Form getTagForm(@PathVariable long tagId) {
        Tag t = contentService.getTag(tagId);
        Form fm = new Form("tag", "标签", "/admin/tag");
        fm.addField(new HiddenField<>("id", t.getId()));
        fm.addField(new TextField<>("name", "名称", t.getName()));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postTagForm(@Valid TagForm form, BindingResult result) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            if (form.getId() == null) {
                if (contentService.getTag(form.getName()) == null) {
                    contentService.addTag(form.getName());
                }
                {
                    ri.addMessage("标签[" + form.getId() + "]已存在");
                    ri.setOk(false);
                }
            } else {
                Tag tag = contentService.getTag(form.getId());
                if (tag == null) {
                    ri.addMessage("标签[" + form.getId() + "]不存在");
                    ri.setOk(false);
                } else {
                    if (contentService.getTag(form.getName()) == null) {
                        contentService.setTagName(form.getId(), form.getName());
                    } else {
                        ri.addMessage("标签[" + form.getId() + "]已存在");
                        ri.setOk(false);
                    }
                }
            }
        }
        return ri;
    }

    @Resource
    private RbacService rbacService;
    @Resource
    private AccountService accountService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private ContentService contentService;

    public void setRbacService(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
