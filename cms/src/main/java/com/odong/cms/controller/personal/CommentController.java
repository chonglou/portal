package com.odong.cms.controller.personal;

import com.odong.web.model.SessionItem;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by flamen on 14-1-6下午12:30.
 */
public class CommentController {

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    String getComment(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("commentList", contentService.listCommentByUser(si.getSsUserId()));
        return "personal/comment";
    }
}
