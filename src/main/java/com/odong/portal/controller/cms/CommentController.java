package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Comment;
import com.odong.portal.entity.Log;
import com.odong.portal.form.cms.CommentForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.HiddenField;
import com.odong.portal.web.form.TextAreaField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:22
 */
@Controller("c.cms.comment")
@RequestMapping(value = "/comment")
@SessionAttributes(SessionItem.KEY)
public class CommentController extends PageController {


    @RequestMapping(value = "/a-{articleId}", method = RequestMethod.GET)
    @ResponseBody
    Form getAddCommentForArticle(@PathVariable String articleId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        Form fm = new Form("comment", "添加评论", "/comment/");
        Article a = contentService.getArticle(articleId);
        if (a == null) {
            fm.addData("文章[" + articleId + "]不存在");

        } else {
            fm.setOk(true);
            checkAnonym(fm, si);
            if (fm.isOk()) {
                fm.addField(new HiddenField<>("article", articleId));
                fm.addField(new HiddenField<Long>("comment", null));
                fm.addField(new TextAreaField("content", "内容"));
                fm.setCaptcha(true);
            }
        }
        return fm;
    }

    @RequestMapping(value = "/c-{commentId}", method = RequestMethod.GET)
    @ResponseBody
    Form getAddComment(@PathVariable long commentId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        Comment comment = contentService.getComment(commentId);
        Form fm = new Form("comment", "添加评论", "/comment/");
        if (comment == null) {
            fm.addData("评论[" + commentId + "]不存在");
        } else {
            fm.setOk(true);
            checkAnonym(fm, si);
            if (fm.isOk()) {
                fm.addField(new HiddenField<>("article", comment.getArticle()));
                fm.addField(new HiddenField<>("comment", commentId));
                fm.addField(new TextAreaField("content", "内容"));
                fm.setCaptcha(true);
            }
        }
        return fm;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postComment(@Valid CommentForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si, HttpServletRequest request) {
        ResponseItem ri = formHelper.check(result, request, true);
        if (ri.isOk()) {
            checkAnonym(ri, si);
        }
        if (ri.isOk()) {
            contentService.addComment(si.getSsUserId(), form.getArticle(), form.getComment(), form.getContent());
        }
        return ri;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseItem deleteComment(@PathVariable long commentId, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        Comment comment = contentService.getComment(commentId);
        if (comment == null) {
            ri.addData("评论[" + comment + "]不存在");
        } else if (comment.getUser() == null) {
            if (si.isSsAdmin()) {
                ri.setOk(true);
            } else {
                ri.addData("匿名评论，您无权删除");
            }
        } else {
            if (comment.getUser().equals(si.getSsUserId()) || si.isSsAdmin()) {
                ri.setOk(true);
            } else {
                ri.addData("您无权删除该评论");
            }
        }
        if (ri.isOk()) {
            contentService.delComment(commentId);
            logService.add(si.getSsUserId(), "删除评论[" + commentId + "]", Log.Type.INFO);
        }
        return ri;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    void getComment(@PathVariable long commentId, HttpServletResponse response) throws IOException {
        response.sendRedirect("/article/" + contentService.getComment(commentId).getArticle() + "#" + commentId);
    }


    private void checkAnonym(ResponseItem ri, SessionItem si) {
        if (si.getSsUserId() == null && !siteService.getBoolean("site.allowAnonym")) {

            ri.setOk(false);
            ri.addData("当前系统不允许匿名评论，您需要登陆");

        }
    }


}
