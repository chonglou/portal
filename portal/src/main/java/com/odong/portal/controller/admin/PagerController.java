package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.PagerForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.SelectField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-5
 * Time: 下午1:38
 */
@Controller("c.admin.pager")
@RequestMapping(value = "/admin/pager")
@SessionAttributes(SessionItem.KEY)
public class PagerController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Form getSizeForm() {
        Form fm = new Form("pager", "分页设置", "/admin/pager/");

        SelectField<Integer> hotTagCount = new SelectField<>("hotTagCount", "热门标签数", siteService.getInteger("site.hotTagCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            hotTagCount.addOption(i + "个", i);
        }
        fm.addField(hotTagCount);

        SelectField<Integer> hotArticleCount = new SelectField<>("latestArticleCount", "左侧最新文章数", siteService.getInteger("site.latestArticleCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            hotArticleCount.addOption(i + "篇", i);
        }
        fm.addField(hotArticleCount);

        SelectField<Integer> latestCommentCount = new SelectField<>("latestCommentCount", "左侧最新评论数", siteService.getInteger("site.latestCommentCount"));
        for (int i : new int[]{5, 10, 20, 30, 50}) {
            latestCommentCount.addOption(i + "条", i);
        }
        fm.addField(latestCommentCount);

        SelectField<Integer> archiveCount = new SelectField<>("archiveCount", "左侧最近归档数", siteService.getInteger("site.archiveCount"));
        for (int i : new int[]{3, 6, 9, 12, 15, 18, 24}) {
            archiveCount.addOption(i + "个月", i);
        }
        fm.addField(archiveCount);

        SelectField<Integer> articlePageSize = new SelectField<>("articlePageSize", "每页文章数", siteService.getInteger("site.articlePageSize"));
        for (int i : new int[]{20, 30, 50, 80, 100, 150, 200}) {
            articlePageSize.addOption(i + "篇", i);
        }
        fm.addField(articlePageSize);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postSizeForm(@Valid PagerForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.archiveCount", form.getArchiveCount());
            siteService.set("site.articlePageSize", form.getArticlePageSize());
            siteService.set("site.latestArticleCount", form.getLatestArticleCount());
            siteService.set("site.hotTagCount", form.getHotTagCount());
            siteService.set("site.latestCommentCount", form.getLatestCommentCount());
            logService.add(si.getSsUserId(), "修改用户注册协议", Log.Type.INFO);
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

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
