package com.odong.portal.controller.cms;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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
    String getUser(Map<String, Object> map, @PathVariable long userId) {
        map.put("navBars", getNavBars());
        //TODO 分页
        map.put("articleList", contentService.listArticleByAuthor(userId));
        List<NavBar> navBars = new ArrayList<>();

        NavBar nbUser = new NavBar("用户列表");
        for(User u : accountService.listUser()){
            nbUser.add(u.getEmail()+"["+u.getUsername()+"]", "/user/"+u.getId());
        }
        navBars.add(nbUser);
        map.put("navBars", navBars);

        fillSiteInfo(map);
        User u = accountService.getUser(userId);
        map.put("user", u);
        map.put("title", "用户-[" + u.getUsername() + "]");
        return "cms/user";
    }
}