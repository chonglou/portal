package com.odong.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odong.core.entity.User;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.UserService;
import com.odong.core.util.FormHelper;
import com.odong.core.util.HttpClient;
import com.odong.platform.util.CacheService;
import com.odong.web.model.ResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午5:44
 */

@Controller("platform.c.oAuth")
@RequestMapping(value = "/oauth")
public class OauthController {
    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    ResponseItem postGoogleAuth(
            @RequestParam("info") String info,
            @RequestParam("token") String token,
            @RequestParam("code") String code,
            HttpSession session) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (formHelper.isLogin(session)) {
            ri.addData("已经登录");
            ri.setOk(true);
            return ri;
        }
        GoogleAuthProfile gap = cacheService.getGoogleAuthProfile();
        if (gap != null && gap.isEnable()) {
            logger.debug("Google 登录\ninfo={}\ntoken={}\ncode={}", info, token, code);

            String check = httpClient.get("https://www.googleapis.com/oauth2/v1/tokeninfo?id_token=" + info);
            ObjectMapper mapper = new ObjectMapper();

            try {
                Map<String, String> map = mapper.readValue(check, Map.class);
                if (map != null && map.get("error") == null) {
                    String id = map.get("user_id");

                    User user = userService.getUser(id, User.Type.GOOGLE);
                    long uid;
                    if (user == null) {
                        uid = userService.addGoogleUser(id, token);
                        user = userService.getUser(uid);
                    } else {
                        uid = user.getId();
                        if (!token.equals(user.getExtra())) {
                            userService.setUserExtra(uid, token);
                            logger.debug("更新google用户{}的token", id);
                        }
                    }
                    formHelper.login(session, User.Type.GOOGLE, uid, user.getUsername(), user.getLogo(), user.getEmail());
                    ri.setOk(true);
                    return ri;

                } else {
                    ri.addData("无效的info参数");
                }

            } catch (IOException e) {
                logger.error("解析JSON出错", e);
            }


        } else {
            ri.addData("未启用google登录验证");
        }

        return ri;
    }

    @RequestMapping(value = "/qq", method = RequestMethod.GET)
    String qqAuth() {
        return "oauth/qq";
    }

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem qqAuth(@RequestParam("id") String id,
                        @RequestParam("token") String token,
                        @RequestParam("name") String name,
                        @RequestParam("logo") String logo,
                        HttpSession session) {

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (formHelper.isLogin(session)) {
            ri.addData("已经登录");
            ri.setOk(true);
            return ri;
        }

        if (!checkQqOpenId(token, id)) {
            ri.addData("数据格式不正确");
            return ri;
        }

        logger.debug("QQ登录 openid={} token={} name={}", id, token, name);

        User user = userService.getUser(id, User.Type.QQ);
        long uid;
        if (user == null) {
            uid = userService.addQqUser(id, token, name);
        } else {
            uid = user.getId();

            if (!name.equals(user.getUsername())) {
                userService.setUserName(uid, name);
                logger.info("更新用户{}的昵称", id);
            }
            if (!token.equals(user.getExtra())) {
                userService.setUserExtra(uid, token);
                logger.info("更新Q用户{}的token", id);
            }
        }

        formHelper.login(session, User.Type.QQ, uid, name, logo, user == null ? null : user.getEmail());
        ri.setOk(true);
        return ri;
    }


    private boolean checkQqOpenId(String token, String openId) {
        QqAuthProfile qap = cacheService.getQqAuthProfile();
        if (qap != null && qap.isEnable()) {
            String callback = httpClient.get("https://graph.qq.com/oauth2.0/me?access_token=" + token);
            return callback != null && callback.contains(openId) && callback.contains(qap.getId());
        } else {
            logger.error("未启用qq互联");
        }
        return false;
    }

    private final static Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Resource
    private CacheService cacheService;
    @Resource
    private UserService userService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private HttpClient httpClient;

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
