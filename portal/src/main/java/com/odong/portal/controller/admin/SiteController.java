package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.DomainForm;
import com.odong.portal.form.admin.InfoForm;
import com.odong.portal.form.admin.PagerForm;
import com.odong.portal.form.admin.RegProtocolForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.SelectField;
import com.odong.portal.web.form.TextAreaField;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
            cacheHelper.delete("site/info");
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
    String getInfo() {
        return "admin/info";
    }

    @RequestMapping(value = "/domain", method = RequestMethod.GET)
    @ResponseBody
    Form getDomainForm() {
        Form fm = new Form("domain", "域名设置", "/admin/site/domain");
        fm.addField(new TextField<>("domain", "域名", siteService.getString("site.domain")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/domain", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDomainForm(@Valid DomainForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.domain", form.getDomain());
            logService.add(si.getSsUserId(), "修改站点域名", Log.Type.INFO);
            cacheHelper.delete("site/info");
            cacheHelper.delete("site/domain");
        }
        return ri;
    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    Form getDetailsForm() {
        Form fm = new Form("details", "站点信息编辑", "/admin/site/details");
        fm.addField(new TextField<>("title", "名称", siteService.getString("site.title")));
        fm.addField(new TextField<>("keywords", "关键字", siteService.getString("site.keywords"), "用空格隔开"));
        TextAreaField desc = new TextAreaField("description", "说明", siteService.getString("site.description"));
        desc.setHtml(false);
        fm.addField(desc);
        fm.addField(new TextField<>("copyright", "版权", siteService.getString("site.copyright")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDetailsForm(@Valid InfoForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
            logService.add(si.getSsUserId(), "修改站点基本信息", Log.Type.INFO);
            cacheHelper.delete("site/info");
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
