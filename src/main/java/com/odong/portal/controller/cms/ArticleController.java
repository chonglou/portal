package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.*;
import com.odong.portal.form.cms.ArticleForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller("c.cms.article")
@RequestMapping(value = "/article")
@SessionAttributes(SessionItem.KEY)
public class ArticleController extends PageController {

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    Form getAddArticle(@ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("article", "新增文章", "/article/");
        fm.setOk(true);
        checkLogin(fm, si);
        if (fm.isOk()) {
            fm.addField(new HiddenField<String>("id", null));
            fm.addField(new TextField<String>("title", "标题"));

            TextAreaField summary = new TextAreaField("summary", "摘要");
            summary.setHtml(false);
            summary.setHeight(100);
            fm.addField(summary);

            CheckBoxField<Long> tags = new CheckBoxField<>("tags", "标签");
            for (Tag t : contentService.listTag()) {
                tags.addOption(t.getName(), t.getId(), false);
            }
            fm.addField(tags);

            fm.addField(new TextAreaField("body", "内容"));
            fm.setCaptcha(true);
        }
        return fm;
    }

    @RequestMapping(value = "/{articleId}", method = RequestMethod.PUT)
    @ResponseBody
    Form putArticleEditForm(@PathVariable String articleId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        Article article = contentService.getArticle(articleId);
        Form fm = new Form("article", "编辑文章[" + articleId + "]", "/article/");
        fm.setOk(true);
        checkLogin(fm, si);
        if (fm.isOk()) {
            if (article == null) {
                fm.setOk(false);
                fm.addData("文章[" + articleId + "]不存在");
            } else {
                fm.addField(new HiddenField<>("id", article.getId()));
                fm.addField(new TextField<>("title", "标题", article.getTitle()));
                TextAreaField summary = new TextAreaField("summary", "摘要", article.getSummary());
                summary.setHtml(false);
                fm.addField(summary);

                CheckBoxField<Long> tags = new CheckBoxField<>("tags", "标签");
                for (Tag t : contentService.listTag()) {
                    tags.addOption(t.getName(), t.getId(), contentService.getArticleTag(articleId, t.getId()) != null);
                }
                fm.addField(tags);

                fm.addField(new TextAreaField("body", "内容", article.getBody()));
                fm.setCaptcha(true);
            }
        }
        return fm;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem deleteArticle(@PathVariable String id, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        Article article = contentService.getArticle(id);
        String defA = siteService.getString("site.defArticle");
        if (article == null) {
            ri.addData("文章[" + id + "]不存在");
        } else {
            if (defA.equals(article.getId())) {
                ri.addData("默认文章不能被删除");
            } else {
                ri.setOk(true);
            }
            checkLogin(ri, si);
            if (ri.isOk()) {
                if (article.getAuthor() == si.getSsUserId() || si.isSsAdmin()) {
                    contentService.delArticle(id);
                    logService.add(si.getSsUserId(), "删除文章[" + id + "]", Log.Type.INFO);
                }
            }
        }
        return ri;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postArticle(@Valid ArticleForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        checkLogin(ri, si);
        if (ri.isOk()) {
            if (form.getId() == null || "".equals(form.getId())) {
                String aid = UUID.randomUUID().toString();
                contentService.addArticle(aid, si.getSsUserId(), form.getTitle(), form.getSummary(), form.getBody());
                for (String s : form.getTags().split("-")) {
                    contentService.bindArticleTag(aid, Long.parseLong(s));
                }
            } else {
                Article article = contentService.getArticle(form.getId());
                if (article != null && (article.getAuthor() == si.getSsUserId() || si.isSsAdmin())) {
                    contentService.setArticle(form.getId(), form.getTitle(), form.getSummary(), form.getBody());
                    contentService.delTagByArticle(form.getId());
                    for (String s : form.getTags().split("-")) {
                        contentService.bindArticleTag(form.getId(), Long.parseLong(s));
                    }
                } else {
                    ri.setOk(false);
                    ri.addData("文章[" + form.getId() + "]不存在");
                }
            }

        }
        return ri;
    }

    @RequestMapping(value = "/{articleId}", method = RequestMethod.GET)
    String getArticle(Map<String, Object> map, @PathVariable String articleId, HttpServletResponse response) throws IOException {
        Article a = contentService.getArticle(articleId);
        if (a != null) {
            contentService.setArticleVisits(articleId);
            map.put("article", a);
            map.put("user", accountService.getUser(a.getAuthor()));
            map.put("navBars", getNavBars());
            fillSiteInfo(map);
            map.put("title", a.getTitle());
            map.put("description", a.getSummary());

            map.put("tagList", contentService.listTagByArticle(articleId));
            List<Comment> comments = contentService.listCommentByArticle(articleId);
            Map<Long,User> users = new HashMap<>();
            for(Comment c : comments){
                if(users.get(c.getUser()) == null){
                    users.put(c.getUser(), accountService.getUser(c.getUser()));
                }
            }
            map.put("commentList", comments);
            map.put("userMap", users);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return "cms/article";
    }
}
