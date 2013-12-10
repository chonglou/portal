package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.entity.cms.Statics;
import com.odong.portal.form.cms.StaticsForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by flamen on 13-12-10 上午10:40.
 */

@Controller("c.admin.statics")
@RequestMapping(value = "/admin/statics")
@SessionAttributes(SessionItem.KEY)
public class StaticsController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getTagList(Map<String, Object> map) {
        map.put("staticsList", contentService.listStatics());
        return "admin/statics";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    Form getAdd() {
        Form fm = new Form("statics", "添加静态资源", "/admin/statics/");
        fm.addField(new HiddenField<Long>("id", null));
        RadioField<Statics.Type> type = new RadioField<>("type", "类型", Statics.Type.VIDEO);
        type.addOption("视频", Statics.Type.VIDEO);
        type.addOption("电子书", Statics.Type.BOOK);
        fm.addField(type);
        fm.addField(new TextField<>("name", "名称", null));
        fm.addField(new TextField<>("url", "路径", null));
        TextAreaField taf = new TextAreaField("details", "详细信息", null);
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/{saticId}", method = RequestMethod.GET)
    @ResponseBody
    Form getEdit(@PathVariable long staticId) {
        Statics s = contentService.getStatics(staticId);
        Form fm = new Form("statics", "修改静态资源[" + staticId + "]", "/admin/statics/");

        fm.addField(new HiddenField<Long>("id", s.getId()));
        RadioField<Statics.Type> type = new RadioField<>("type", "类型", s.getType());
        type.addOption("视频", Statics.Type.VIDEO);
        type.addOption("电子书", Statics.Type.BOOK);
        fm.addField(type);
        fm.addField(new TextField<>("name", "名称", s.getName()));
        fm.addField(new TextField<>("url", "路径", s.getUrl()));
        TextAreaField taf = new TextAreaField("details", "详细信息", s.getDetails());
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postStatics(@Valid StaticsForm form, BindingResult result) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            if (form.getId() == null) {
                contentService.addStatics(form.getName(), form.getType(), form.getDetails(), form.getUrl());
            } else {
                Statics s = contentService.getStatics(form.getId());
                if (s != null && s.getType() == form.getType()) {
                    contentService.setStatics(form.getId(), form.getName(), form.getDetails(), form.getUrl());
                } else {
                    ri.setOk(false);
                }
            }
        }
        return ri;
    }

    @RequestMapping(value = "/{staticsId}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem deleteStatics(@PathVariable long staticsId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        contentService.delStatics(staticsId);
        logService.add(si.getSsUserId(), "删除资源" + staticsId, Log.Type.INFO);
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        ri.setOk(true);
        return ri;
    }


    @Resource
    private ContentService contentService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private LogService logService;


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
