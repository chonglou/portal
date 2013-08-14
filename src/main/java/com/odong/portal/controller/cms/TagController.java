package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:22
 */
@Controller("c.cms.tag")
@RequestMapping(value = "/tag")
@SessionAttributes(SessionItem.KEY)
public class TagController extends PageController {
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    String getTag(Map<String, Object> map, @PathVariable long tagId) {
        List<NavBar> navBars = new ArrayList<>();
        NavBar nbTag = new NavBar("标签列表");
        for(Tag t : contentService.listTag()){
            nbTag.add(t.getName(), "/tag/"+t.getId());
        }
        navBars.add(nbTag);
        map.put("navBars", navBars);

        //TODO 分页
        List<Article> articles = contentService.listArticleByTag(tagId);
        map.put("articleList", articles);
        map.put("userMap", getUserMap(articles));
        fillSiteInfo(map);
        Tag t = contentService.getTag(tagId);
        map.put("title", "标签-[" + t.getName() + "]");
        map.put("tag",t);
        return "cms/tag";
    }

}
