package com.odong.platform.controller.admin;

import com.odong.portal.entity.FriendLink;
import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.FriendLinkForm;
import com.odong.portal.model.SessionItem;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午5:37
 */
@Controller("c.admin.friendLink")
@RequestMapping(value = "/admin/friendLink")
@SessionAttributes(SessionItem.KEY)
public class FriendLinkController {
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    Form getAddFriendLink(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("friendLink", "添加友情链接", "/admin/friendLink/");
        fm.addField(new HiddenField<>("id", null));
        fm.addField(new TextField<>("name", "名称"));
        fm.addField(new TextField<>("url", "网址"));
        fm.addField(new TextField<>("logo", "图标"));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getFriendLink(@PathVariable long id, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("friendLink", "修改友情链接[" + id + "]", "/admin/friendLink/");
        FriendLink fl = siteService.getFriendLink(id);
        if (fl == null) {
            fm.addData("友情链接[" + id + "]不存在");
        } else {
            fm.addField(new HiddenField<>("id", fl.getId()));
            fm.addField(new TextField<>("name", "名称", fl.getName()));
            fm.addField(new TextField<>("url", "网址", fl.getUrl()));
            fm.addField(new TextField<>("logo", "图标", fl.getLogo()));
            fm.setOk(true);
        }
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postFriendLink(@Valid FriendLinkForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            if (form.getId() == null) {
                siteService.addFriendLink(form.getName(), form.getUrl(), form.getLogo());
                logService.add(si.getSsUserId(), "添加友情链接[" + form.getName() + "]", Log.Type.INFO);
            } else {
                FriendLink fl = siteService.getFriendLink(form.getId());
                if (fl != null) {
                    siteService.setFriendLink(form.getId(), form.getName(), form.getUrl(), form.getLogo());
                    logService.add(si.getSsUserId(), "更新友情链接[" + form.getId() + "]", Log.Type.INFO);
                } else {
                    ri.setOk(false);
                    ri.addData("友情链接[" + form.getId() + "]不存在");
                }
            }
        }
        return ri;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem deleteFriendLink(@PathVariable long id, @ModelAttribute(SessionItem.KEY) SessionItem si, HttpServletRequest request) {
        FriendLink fl = siteService.getFriendLink(id);
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (fl == null) {
            ri.addData("友情链接[" + id + "]不存在 ");
        } else {
            siteService.delFriendLink(id);
            ri.setOk(true);
            logService.add(si.getSsUserId(), "删除友情链接[" + id + "]", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getCompress(Map<String, Object> map) {
        map.put("friendLinkList", siteService.listFriendLink());
        return "admin/friendLink";
    }

    @Resource
    private FormHelper formHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
