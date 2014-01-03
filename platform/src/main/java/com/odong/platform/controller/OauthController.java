package com.odong.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odong.core.entity.Log;
import com.odong.core.entity.User;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.UserService;
import com.odong.platform.util.CacheService;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        GoogleAuthProfile gap = cacheService.getGoogleAuthProfile();
        if (gap != null && gap.isEnable()) {
            logger.debug("Google 登录\ninfo={}\ntoken={}\ncode={}", info, token, code);

            String check = call(new HttpGet("https://www.googleapis.com/oauth2/v1/tokeninfo?id_token=" + info));
            ObjectMapper mapper = new ObjectMapper();

            try {
                Map<String, String> map = mapper.readValue(check, Map.class);
                if (map != null && map.get("error") == null) {
                    String id = map.get("user_id");
                    OpenId oi = cacheService.getOpenId(id, OpenId.Type.GOOGLE);
                    long uid;
                    if (oi == null) {
                        uid = accountService.addGoogleUser(id, token);
                    } else {
                        uid = oi.getUser();
                        if (!token.equals(oi.getToken())) {
                            accountService.setOpenIdToken(oi.getId(), token);
                            logger.debug("更新google用户{}的token", id);
                        }
                    }
                    login(uid, null, "google", session);
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
        if (session.getAttribute(SessionItem.KEY) != null) {
            ri.addData("已经登录");
            ri.setOk(true);
            return ri;
        }

        if (!checkQqOpenId(token, id)) {
            ri.addData("数据格式不正确");
            return ri;
        }

        logger.debug("QQ登录 openid={} token={} name={}", id, token, name);
        OpenId oi = cacheService.getOpenId(id, OpenId.Type.QQ);

        long uid;
        if (oi == null) {
            uid = userService.addQqUser(id, token, name);
        } else {
            uid = oi.getUser();
            User user = cacheService.getUser(uid);
            if (!name.equals(user.getUsername())) {
                accountService.setUserName(uid, name);
                cacheService.popUser(uid);
                logger.info("更新用户{}的昵称", id);
            }
            if (!token.equals(oi.getToken())) {
                accountService.setOpenIdToken(oi.getId(), token);
                cacheService.popOpenId(id, OpenId.Type.QQ);
                logger.info("更新Q用户{}的token", id);
            }
        }

        login(uid, logo, "qq", session);
        ri.setOk(true);
        return ri;
    }

    void login(long uid, String logo, String type, HttpSession session) {
        User user = userService.getUser(uid);
        SessionItem si = new SessionItem(user.getId(), user.getEmail(), user.getUsername(), logo);
        si.setSsType(type);
        session.setAttribute(SessionItem.KEY, si);
        userService.setUserLastLogin(user.getId());
        logService.add(user.getId(), "用户登陆", Log.Type.INFO);
    }


    private String call(HttpUriRequest request) {


        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            //logger.debug("请求{} 返回状态{}", url, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            EntityUtils.consume(entity);

            String callback = sb.toString();
            logger.debug("返回内容{}", callback);
            return callback;

        } catch (IOException e) {
            logger.error("HTTP Client出错", e);
        }

        return null;
    }

    private boolean checkQqOpenId(String token, String openId) {
        QqAuthProfile qap = cacheService.getQqAuthProfile();
        if (qap != null && qap.isEnable()) {
            String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s", token);
            String callback = call(new HttpGet(url));
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
    private LogService logService;
    @Resource
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
