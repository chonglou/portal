package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.model.SessionItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletResponse;
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


    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    void getComment(@PathVariable long commentId, HttpServletResponse response) throws IOException {
        response.sendRedirect("/article/" + contentService.getComment(commentId).getArticle() + "#" + commentId);
    }
}
