package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.entity.Tag;
import com.odong.portal.form.admin.TagForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.HiddenField;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:40
 */

@Controller("c.admin.tag")
@RequestMapping(value = "/admin/tag")
@SessionAttributes(SessionItem.KEY)
public class TagController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getTagList(Map<String, Object> map) {
        map.put("tagList", contentService.listTag());
        return "admin/tag";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    Form getTagAdd() {
        Form fm = new Form("tag", "添加标签", "/admin/tag/");
        fm.addField(new HiddenField<Long>("id", null));
        fm.addField(new TextField<>("name", "名称", null));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    @ResponseBody
    Form getTagEdit(@PathVariable long tagId) {
        Tag t = contentService.getTag(tagId);
        Form fm = new Form("tag", "修改标签["+tagId+"]", "/admin/tag/");

        fm.addField(new HiddenField<>("id", t.getId()));
        fm.addField(new TextField<>("name", "名称", t.getName()));
        fm.setOk(true);
        return fm;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postTag(@Valid TagForm form, BindingResult result) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            if (form.getId() == null) {

                if (contentService.getTag(form.getName()) == null) {
                    contentService.addTag(form.getName(), false);
                } else {
                    ri.addData("标签[" + form.getName() + "]已存在");
                    ri.setOk(false);
                }
            } else {
                Tag tag = contentService.getTag(form.getId());
                if (tag == null) {
                    ri.addData("标签[" + form.getId() + "]不存在");
                    ri.setOk(false);
                } else {
                    if (contentService.getTag(form.getName()) == null) {
                        contentService.setTagName(form.getId(), form.getName());
                    } else {
                        ri.addData("标签[" + form.getName() + "]已存在");
                        ri.setOk(false);
                    }
                }
            }
        }
        return ri;
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem deleteTag(@PathVariable long tagId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        if (contentService.countArticleByTag(tagId) == 0) {
            Tag tag = contentService.getTag(tagId);
            if (tag.isKeep()) {
                ri.addData("标签[" + tag.getName() + "]是内置标签");
            } else {
                contentService.delTag(tagId);
                logService.add(si.getSsUserId(), "删除标签[" + tagId + "]", Log.Type.INFO);
                ri.setOk(true);
            }
        } else {
            ri.addData("标签[" + tagId + "]正在被使用");
        }
        return ri;
    }

    @Resource
    private SiteService siteService;
    @Resource
    private ContentService contentService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private LogService logService;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }
}
