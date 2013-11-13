package com.odong.portal.controller;

import com.odong.portal.entity.Log;
import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.web.ResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午5:44
 */

@Controller("c.oAuth")
@RequestMapping(value = "/oauth")
public class OauthController extends PageController {
    @RequestMapping(value = "/qq", method = RequestMethod.GET)
    String getQqAuth(Map<String,Object> map) throws IOException{
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        return "oauth/qq";
    }
    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem qqAuth(@RequestParam("id")String id,
                        @RequestParam("token") String token,
                        @RequestParam("name") String name,
                        HttpSession session) {
        logger.debug("QQ登录 openid={} token={} name={}", id, token, name);
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);

        OpenId oi = accountService.getOpenId(id, OpenId.Type.QQ);
        long uid = oi == null ? accountService.addQQUser(id, token, name) : oi.getUser();
        User user = accountService.getUser(uid);
        if(!name.equals(user.getUsername())){
            accountService.setUserName(uid, name);
        }
        SessionItem si = new SessionItem(uid, user.getEmail(), name);
        si.setSsLocal(false);
        session.setAttribute(SessionItem.KEY, si);
        logService.add(uid, "用户登陆", Log.Type.INFO);
        ri.setOk(true);

        return ri;
    }

    private final static Logger logger = LoggerFactory.getLogger(OauthController.class);

}
