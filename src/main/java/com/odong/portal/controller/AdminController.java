package com.odong.portal.controller;

import com.odong.portal.entity.Tag;
import com.odong.portal.form.TagForm;
import com.odong.portal.model.ResponseItem;
import com.odong.portal.service.ContentService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午12:14
 */
@Controller("/admin")
public class AdminController {
    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    @ResponseBody
    Form getTagForm(){
        Form fm = new Form("tag", "标签", "/admin/tag");
        fm.addField(new TextField<>("name", "名称", ""));
        fm.setOk(true);
        return fm;
    }
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postTagForm(@Valid TagForm form, BindingResult result){
        ResponseItem ri = formHelper.check(result);
        if(ri.isOk()){
            if(form.getId() == null){
                if(contentService.getTag(form.getName()) == null){
                    contentService.addTag(form.getName());
                }
                {
                    ri.add("标签[" + form.getId() + "]已存在");
                    ri.setOk(false);
                }
            }
            else {
                Tag tag = contentService.getTag(form.getId());
                if(tag == null){
                    ri.add("标签["+form.getId()+"]不存在");
                    ri.setOk(false);
                }
                else {
                    if(contentService.getTag(form.getName()) == null){
                        contentService.setTagName(form.getId(), form.getName());
                    }
                    else {
                        ri.add("标签[" + form.getId() + "]已存在");
                        ri.setOk(false);
                    }
                }
            }
        }
        return ri;
    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private ContentService contentService;

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
