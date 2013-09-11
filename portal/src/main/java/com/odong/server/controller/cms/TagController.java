package com.odong.server.controller.cms;

import com.odong.server.controller.PageController;
import com.odong.server.entity.Article;
import com.odong.server.entity.Tag;
import com.odong.server.model.SessionItem;
import com.odong.server.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    String getTag(Map<String, Object> map, @PathVariable long tagId, HttpServletResponse response) throws IOException {

        Tag tag = contentService.getTag(tagId);
        if (tag != null) {
            contentService.setTagVisits(tagId);
            List<NavBar> navBars = new ArrayList<>();

            NavBar nbTag = new NavBar("标签列表");
            nbTag.setType(NavBar.Type.LIST);
            for (Tag t : contentService.listTag()) {
                nbTag.add(t.getName(), "/tag/" + t.getId());
            }

            navBars.add(nbTag);
            map.put("navBars", navBars);

            //TODO 分页
            List<Article> articles = contentService.listArticleByTag(tagId);
            map.put("articleList", articles);
            map.put("userMap", getUserMap(articles));
            fillSiteInfo(map);
            map.put("title", "标签-[" + tag.getName() + "]");
            map.put("tag", tag);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return "cms/tag";
    }

}
