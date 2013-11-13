package com.odong.portal.controller;

import com.odong.portal.entity.Log;
import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.model.SessionItem;
import com.odong.portal.model.profile.QQAuthProfile;
import com.odong.portal.web.ResponseItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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

@Controller("c.oAuth")
@RequestMapping(value = "/oauth")
public class OauthController extends PageController {
    @RequestMapping(value = "/qq", method = RequestMethod.GET)
    String getQqAuth(Map<String, Object> map) throws IOException {
        map.put("navBars", getNavBars());
        fillSiteInfo(map);
        return "oauth/qq";
    }

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem qqAuth(@RequestParam("id") String id,
                        @RequestParam("token") String token,
                        @RequestParam("name") String name,
                        HttpSession session) {
        logger.debug("QQ登录 openid={} token={} name={}", id, token, name);
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (!checkQqOpenId(token, id)) {
            ri.addData("数据格式不正确");
            return ri;
        }

        OpenId oi = accountService.getOpenId(id, OpenId.Type.QQ);

        User user;
        if (oi == null) {
            long uid = accountService.addQQUser(id, token, name);
            user = accountService.getUser(uid);
        } else {
            long uid = oi.getUser();
            user = accountService.getUser(uid);
            if (!name.equals(user.getUsername())) {
                accountService.setUserName(uid, name);
                logger.info("更新{}的昵称", id);
            }
            if (!token.equals(oi.getToken())) {
                accountService.setOpenIdToken(oi.getId(), token);
                logger.info("更新{}的token", id);
            }
        }


        SessionItem si = new SessionItem(user.getId(), user.getEmail(), name);
        si.setSsLocal(false);
        session.setAttribute(SessionItem.KEY, si);
        accountService.setUserLastLogin(user.getId());
        logService.add(user.getId(), "用户登陆", Log.Type.INFO);
        ri.setOk(true);

        return ri;
    }

    private boolean checkQqOpenId(String token, String openId) {
        QQAuthProfile qap = cacheService.getQQAuthProfile();
        if (qap != null && qap.isEnable()) {
            String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s", token);
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse response = client.execute(new HttpGet(url))) {
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

                return callback.contains(openId) && callback.contains(qap.getId());

            } catch (IOException e) {
                logger.error("查询token出错", e);
            }
        }
        else {
            logger.error("未启用qq互联");
        }
        return false;
    }

    private final static Logger logger = LoggerFactory.getLogger(OauthController.class);

}
