package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.Card;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.Page;
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

        Tag tag = cacheHelper.get("tag/"+tagId, Tag.class, null, ()->contentService.getTag(tagId));
        if (tag != null) {
            contentService.setTagVisits(tagId);
            List<NavBar> navBars = new ArrayList<>();

            navBars.add(cacheHelper.get("navBar/tags", NavBar.class, null,()->{
                NavBar nb = new NavBar("标签列表");
                nb.setType(NavBar.Type.LIST);
                for (Tag t : contentService.listTag()) {
                    nb.add(t.getName(), "/tag/" + t.getId());
                }
                return nb;
            }));
            map.put("navBars", navBars);

            map.put("articleList", cacheHelper.get("articleCard/tag/"+tagId, ArrayList.class, null, ()->{
                ArrayList<Card> cards = new ArrayList<>();
                //FIXME 分页
                contentService.listArticleByTag(tagId).forEach((a)->cards.add(a.toCard()));
                return cards;
            }));
            fillSiteInfo(map);
            map.put("title", "标签-[" + tag.getName() + "]");
            map.put("tag", tag);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return "cms/tag";
    }

}
