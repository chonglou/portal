package com.odong.platform.controller.personal;

import com.odong.web.model.Link;
import com.odong.web.model.Page;
import com.odong.web.model.SessionItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 14-1-2下午9:45.
 */
@Controller("platform.c.personal.self")
@RequestMapping(value = "/personal")
public class SelfController {
     @RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
     Map<String, Object> getItems(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        List<Link> pages = new ArrayList<>();
        if (session.getAttribute(SessionItem.KEY) == null) {
            pages.add(new Link("用户登录", "/personal/login"));
            pages.add(new Link("账户注册", "/personal/register"));
            pages.add(new Link("账户激活", "/personal/active"));
            pages.add(new Link("重置密码", "/personal/resetPwd"));
            map.put("ok", false);
        } else {
            SessionItem si = (SessionItem) session.getAttribute(SessionItem.KEY);
            pages.add(new Link("用户中心", "/personal/self"));
            pages.add(new Link("安全退出", "/personal/logout"));
            map.put("logo", si.getSsLogo());
            map.put("name", si.getSsUsername());
            map.put("type", si.getSsType());
            map.put("ok", true);
        }

        map.put("items", pages);

        return map;
    }

}
