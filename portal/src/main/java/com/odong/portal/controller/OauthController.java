package com.odong.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odong.portal.entity.Log;
import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.model.profile.GoogleAuthProfile;
import com.odong.portal.model.profile.QQAuthProfile;
import com.odong.portal.web.ResponseItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
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
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    void getGoogleAuth(@RequestParam("state") String state,
                       @RequestParam("code") String code,
                       HttpSession session, HttpServletResponse response) throws IOException {

        if (state.equals(session.getAttribute("googleState"))) {
            session.removeAttribute("googleState");

            String callback = checkGoogleCode(code);
            if (callback != null) {
                Map<String, String> map = null;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    map = (Map<String, String>) mapper.readValue(callback, Map.class);
                } catch (IOException e) {
                    logger.error("解析JSON出错", e);
                }
                if (map != null) {
                    String email = map.get("email");
                    String openId = map.get("sub");
                    OpenId oi = accountService.getOpenId(openId, OpenId.Type.GOOGLE);
                    long uid = oi == null ? accountService.addGoogleUser(openId, email) : oi.getUser();
                    login(uid, session, "google");
                    response.sendRedirect("/");
                    return;
                }
            }
        } else {
            logger.error("状态不对");
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);

    }


    @RequestMapping(value = "/google", method = RequestMethod.POST)
    void postGoogleAuth(HttpSession session, HttpServletResponse response) throws IOException {
        GoogleAuthProfile gap = cacheService.getGoogleAuthProfile();
        if (gap != null && gap.isEnable()) {
            String state = new BigInteger(130, new SecureRandom()).toString(32);
            session.setAttribute("googleState", state);
            StringBuilder sb = new StringBuilder();
            sb.append("https://accounts.google.com/o/oauth2/auth?client_id=");
            sb.append(gap.getId());
            sb.append("&response_type=code&scope=openid%20email&redirect_uri=");
            sb.append(gap.getUri());
            sb.append("&state=");
            sb.append(state);

            response.sendRedirect(sb.toString());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    /*
    @RequestMapping(value = "/qq", method = RequestMethod.GET)
    String getQqAuth(Map<String, Object> map) throws IOException {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        return "oauth/qq";
    }
    */


    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem qqAuth(@RequestParam("id") String id,
                        @RequestParam("token") String token,
                        @RequestParam("name") String name,
                        HttpSession session) {

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if(session.getAttribute(SessionItem.KEY) != null){
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
            uid = accountService.addQQUser(id, token, name);
        } else {
            uid = oi.getUser();
            User user = cacheService.getUser(uid);
            if (!name.equals(user.getUsername())) {
                accountService.setUserName(uid, name);
                cacheService.popUser(uid);
                logger.info("更新{}的昵称", id);
            }
            if (!token.equals(oi.getToken())) {
                accountService.setOpenIdToken(oi.getId(), token);
                cacheService.popOpenId(id, OpenId.Type.QQ);
                logger.info("更新{}的token", id);
            }
        }

        login(uid, session, "qq");
        ri.setOk(true);
        return ri;
    }

    void login(long uid, HttpSession session, String type) {
        User user = cacheService.getUser(uid);
        SessionItem si = new SessionItem(user.getId(), user.getEmail(), user.getUsername());
        si.setSsType(type);
        session.setAttribute(SessionItem.KEY, si);
        accountService.setUserLastLogin(user.getId());
        logService.add(user.getId(), "用户登陆", Log.Type.INFO);
    }

    private String checkGoogleCode(String code) {
        GoogleAuthProfile gap = cacheService.getGoogleAuthProfile();
        if (gap != null && gap.isEnable()) {
            String url = String.format("https://accounts.google.com/o/oauth2/token?code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code", code, gap.getId(), gap.getSecret(), gap.getUri());
            return call(url, "POST");
        } else {
            logger.error("未启用google认证");
        }
        return null;
    }


    private String call(String url, String method) {
        HttpUriRequest request = null;
        switch (method) {
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                request = new HttpPost(url);
                break;

        }
        if (request != null) {
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(request)) {
                logger.debug("请求{} 返回状态{}", url, response.getStatusLine());
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
        }
        return null;
    }

    private boolean checkQqOpenId(String token, String openId) {
        QQAuthProfile qap = cacheService.getQQAuthProfile();
        if (qap != null && qap.isEnable()) {
            String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s", token);
            String callback = call(url, "GET");
            return callback != null && callback.contains(openId) && callback.contains(qap.getId());
        } else {
            logger.error("未启用qq互联");
        }
        return false;
    }

    private final static Logger logger = LoggerFactory.getLogger(OauthController.class);

}
