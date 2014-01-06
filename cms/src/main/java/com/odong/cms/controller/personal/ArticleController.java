package com.odong.cms.controller.personal;

import com.odong.web.model.SessionItem;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by flamen on 14-1-6下午12:30.
 */
public class ArticleController {


    @RequestMapping(value = "/article", method = RequestMethod.GET)
    String getArticle(Map<String, Object> map, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        map.put("manager", manager);
        map.put("articleList", si.isSsAdmin() ? contentService.listArticle() : contentService.listArticleByAuthor(si.getSsUserId()));
        return "personal/article";
    }
}
