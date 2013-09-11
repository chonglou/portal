package com.odong.server.controller.cms;

import com.odong.server.controller.PageController;
import com.odong.server.entity.User;
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
 * Time: 下午4:09
 */
@Controller("c.cms.user")
@RequestMapping(value = "/user")
@SessionAttributes(SessionItem.KEY)
public class UserController extends PageController {
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    String getUser(Map<String, Object> map, @PathVariable long userId, HttpServletResponse response) throws IOException {

        User user = accountService.getUser(userId);
        if (user != null) {
            contentService.setUserVisits(userId);
            map.put("navBars", getNavBars());
            //TODO 分页
            map.put("articleList", contentService.listArticleByAuthor(userId));
            List<NavBar> navBars = new ArrayList<>();

            NavBar nbUser = new NavBar("用户列表");
            nbUser.setType(NavBar.Type.LIST);
            for (User u : accountService.listUser()) {
                nbUser.add(u.getUsername(), "/user/" + u.getId());
            }
            navBars.add(nbUser);
            map.put("navBars", navBars);

            fillSiteInfo(map);
            map.put("user", user);
            map.put("title", "用户-[" + user.getUsername() + "]");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        return "cms/user";
    }
}
