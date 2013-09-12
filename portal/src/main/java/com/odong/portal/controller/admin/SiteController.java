package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.AllowForm;
import com.odong.portal.form.admin.InfoForm;
import com.odong.portal.form.admin.PagerForm;
import com.odong.portal.form.admin.RegProtocolForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("c.admin.site")
@RequestMapping(value = "/admin/site")
@SessionAttributes(SessionItem.KEY)
public class SiteController {
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("site.startup", cacheHelper.get("startup", Date.class, null, ()->siteService.getObject("site.startup", Date.class)));
        map.put("site.cache", cacheHelper.status());
        map.put("created", new Date());
        return map;
    }
    @RequestMapping(value = "/pager", method = RequestMethod.GET)
    @ResponseBody
    Form getSizeForm() {
        Form fm = new Form("pager", "分页设置", "/admin/site/pager");

        SelectField<Integer> hotTagCount = new SelectField<Integer>("hotTagCount", "热门标签数", siteService.getInteger("site.hotTagCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            hotTagCount.addOption(i + "个", i);
        }
        fm.addField(hotTagCount);

        SelectField<Integer> hotArticleCount = new SelectField<Integer>("hotArticleCount", "左侧热门文章数", siteService.getInteger("site.hotArticleCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            hotArticleCount.addOption(i + "篇", i);
        }
        fm.addField(hotArticleCount);

        SelectField<Integer> latestCommentCount = new SelectField<Integer>("latestCommentCount", "左侧最新评论数", siteService.getInteger("site.latestCommentCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            latestCommentCount.addOption(i + "条", i);
        }
        fm.addField(latestCommentCount);

        SelectField<Integer> archiveCount = new SelectField<Integer>("archiveCount", "左侧最近归档", siteService.getInteger("site.archiveCount"));
        for (int i : new int[]{3, 6, 9, 12, 15, 18, 24}) {
            archiveCount.addOption(i + "个月", i);
        }
        fm.addField(archiveCount);

        SelectField<Integer> articlePageSize = new SelectField<Integer>("articlePageSize", "每页文章数", siteService.getInteger("site.articlePageSize"));
        for (int i : new int[]{20, 30, 50, 80, 100, 150, 200}) {
            articlePageSize.addOption(i + "篇", i);
        }
        fm.addField(articlePageSize);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/pager", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSizeForm(@Valid PagerForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.archiveCount", form.getArchiveCount());
            siteService.set("site.articlePageSize", form.getArticlePageSize());
            siteService.set("site.hotArticleCount", form.getHotArticleCount());
            siteService.set("site.hotTagCount", form.getHotTagCount());
            siteService.set("site.latestCommentCount", form.getLatestCommentCount());
            logService.add(si.getSsUserId(), "修改用户注册协议", Log.Type.INFO);
        }
        return ri;
    }

    @RequestMapping(value = "/regProtocol", method = RequestMethod.GET)
    @ResponseBody
    Form getRegProtocolForm() {
        Form fm = new Form("regProtocol", "用户注册协议", "/admin/site/regProtocol");
        fm.addField(new TextAreaField("protocol", "用户协议", siteService.getString("site.regProtocol")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/regProtocol", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegProtocolForm(@Valid RegProtocolForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.regProtocol", form.getProtocol());
            logService.add(si.getSsUserId(), "修改用户注册协议", Log.Type.INFO);
        }
        return ri;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Form getInfoForm() {
        Form fm = new Form("info", "站点信息编辑", "/admin/site/info");
        fm.addField(new TextField<>("title", "名称", siteService.getString("site.title")));
        fm.addField(new TextField<>("keywords", "关键字", siteService.getString("site.keywords"), "用空格隔开"));
        TextAreaField desc = new TextAreaField("description", "说明", siteService.getString("site.description"));
        desc.setHtml(false);
        fm.addField(desc);
        fm.addField(new TextField<>("copyright", "版权", siteService.getString("site.copyright")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postInfoForm(@Valid InfoForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
            logService.add(si.getSsUserId(), "修改站点基本信息", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/state", method = RequestMethod.GET)
    @ResponseBody
    Form getAllow() {
        Form fm = new Form("siteState", "网站状态", "/admin/site/state");
        RadioField<Boolean> login = new RadioField<>("allowLogin", "登陆", siteService.getBoolean("site.allowLogin"));
        login.addOption("允许", true);
        login.addOption("禁止", false);
        RadioField<Boolean> register = new RadioField<>("allowRegister", "注册", siteService.getBoolean("site.allowRegister"));
        register.addOption("允许", true);
        register.addOption("禁止", false);
        RadioField<Boolean> anonym = new RadioField<>("allowAnonym", "匿名用户", siteService.getBoolean("site.allowAnonym"));
        anonym.addOption("允许", true);
        anonym.addOption("禁止", false);
        fm.addField(login);
        fm.addField(register);
        fm.addField(anonym);
        fm.setOk(true);
        return fm;
    }


    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAllow(@Valid AllowForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.allowRegister", form.isAllowRegister());
            siteService.set("site.allowLogin", form.isAllowLogin());
            siteService.set("site.allowAnonym", form.isAllowAnonym());
            logService.add(si.getSsUserId(), "变更站点权限 注册=>[" + form.isAllowRegister() + "] 登陆=>[" + form.isAllowLogin() + "] 匿名用户=>[" + form.isAllowAnonym() + "]", Log.Type.INFO);
        }
        return ri;

    }


    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private CacheHelper cacheHelper;

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
