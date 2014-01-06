package com.odong.cms.controller.admin;

import com.odong.core.entity.Log;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.TextAreaField;
import com.odong.web.model.form.TextField;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by flamen on 14-1-5下午9:28.
 */
public class GoogleController {
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    @ResponseBody
    Form getGoogleForm() {
        Form fm = new Form("google", "Google Web 设置", "/admin/site/google");
        fm.addField(new TextField<>("valid", "验证文件名", siteService.getString("site.google.valid")));
        TextAreaField search = new TextAreaField("search", "自定义搜索代码", siteService.getString("site.google.search"));
        search.setHtml(false);
        fm.addField(search);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postGoogleForm(@Valid GoogleForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.google.valid", form.getValid().trim());
            siteService.set("site.google.search", form.getSearch());
            logService.add(si.getSsUserId(), "修改google配置", Log.Type.INFO);
            cacheService.popGoogleSearch();
            cacheService.popGoogleValidCode();
        }
        return ri;
    }
}
