package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Tag;
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
 * Date: 13-8-13
 * Time: 下午2:22
 */
@Controller("c.cms.tag")
@RequestMapping(value = "/tag")
@SessionAttributes(SessionItem.KEY)
public class TagController extends PageController {


    @RequestMapping(value = "/tag/{tagId}", method = RequestMethod.GET)
    String getTag(Map<String, Object> map, @PathVariable long tagId) {
        map.put("navBars", navBars());
        //TODO 分页
        map.put("articles", contentService.listArticleByTag(tagId));
        fillSiteInfo(map);
        Tag t = contentService.getTag(tagId);
        map.put("title", "标签[" + t.getName() + "](" + t.getVisits() + ")");
        return "falls";
    }
}
