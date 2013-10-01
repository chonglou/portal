package com.odong.portal.controller.admin;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Log;
import com.odong.portal.entity.User;
import com.odong.portal.form.admin.AuthorForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.LogService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.HiddenField;
import com.odong.portal.web.form.SelectField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-18
 * Time: 上午12:42
 */
@Controller("c.admin.article")
@RequestMapping(value = "/admin/article")
@SessionAttributes(SessionItem.KEY)
public class ArticleController {
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Form getForm(@PathVariable long id) {
        Form fm = new Form("article", "变更文章[" + id + "]的作者", "/admin/article/");
        Article a = contentService.getArticle(id);
        if (a != null) {
            fm.addField(new HiddenField<Long>("article", id));
            SelectField<Long> user = new SelectField<Long>("author", "用户", a.getAuthor());
            accountService.listUser().forEach((u) -> {
                if (u.getState() == User.State.ENABLE && !u.getEmail().equals(manager)) {
                    user.addOption(u.toString(), u.getId());
                }
            });
            user.setWidth(220);
            fm.addField(user);
            fm.setOk(true);
        }
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postForm(@Valid AuthorForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            contentService.setArticleAuthor(form.getArticle(), form.getAuthor());
            logService.add(si.getSsUserId(), "变更文章[" + form.getArticle() + "]的作者为[" + form.getAuthor() + "]", Log.Type.INFO);
        }
        return ri;
    }

    @Resource
    private LogService logService;
    @Resource
    private ContentService contentService;
    @Resource
    private AccountService accountService;
    @Resource
    private FormHelper formHelper;
    @Value("${app.manager}")
    private String manager;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
