package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Article;
import com.odong.portal.model.SessionItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Map;

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


    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    String getArticle(Map<String, Object> map, @PathVariable long articleId) {
        map.put("navBars", navBars());
        map.put("tags", contentService.listTagByArticle(articleId));
        map.put("comments", contentService.listCommentByArticle(articleId));
        Article a = contentService.getArticle(articleId);
        map.put("article", a);
        fillSiteInfo(map);
        map.put("title", a.getTitle());
        map.put("description", a.getSummary());

        return "falls";
    }
}
