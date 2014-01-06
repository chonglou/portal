package com.odong.core.util;

import com.google.code.kaptcha.Constants;
import com.odong.core.entity.Log;
import com.odong.core.entity.User;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.service.LogService;
import com.odong.core.service.UserService;
import com.odong.web.model.Link;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by flamen on 14-1-2下午8:47.
 */
@Component("core.formHelper")
public class FormHelper {
    public void login(HttpSession session, User.Type type, long userId, String username, String logo, String email, boolean isAdmin) {
        SessionItem si = new SessionItem(userId, username, logo, email);
        si.setSsType(type);
        si.setSsAdmin(isAdmin);
        session.setAttribute(sessionKey, si);
        userService.setUserLastLogin(userId);
        logService.add(userId, "用户登陆", Log.Type.INFO);
    }

    public void logout(HttpSession session) {
        SessionItem si = getSessionItem(session);
        session.setAttribute(sessionKey, null);
        logService.add(si.getSsUserId(), "注销登陆", Log.Type.INFO);
    }

    public boolean isLogin(HttpSession session) {
        return getSessionItem(session) != null;
    }

    public SessionItem getSessionItem(HttpSession session) {
        return (SessionItem) session.getAttribute(sessionKey);
    }

    public Page getPage(HttpSession session) {
        Page page = cacheService.getPage();

        page.setDomain(cacheService.getSiteDomain());
        page.setTitle(cacheService.getSiteTitle());

        page.setGoogleAuth(cacheService.getGoogleAuthProfile());
        page.setQqAuth(cacheService.getQqAuthProfile());

        pluginUtil.foreach((Plugin plugin) -> {
            page.getSideBars().addAll(plugin.getSideBars());
        });

        pluginUtil.foreach((Plugin plugin) -> {
            page.getTopLinks().addAll(plugin.getTopLinks());
        });
        page.getTopLinks().add(new Link("用户中心", "/personal/self"));
        page.getTopLinks().add(new Link("网站地图", "/sitemap"));
        page.getTopLinks().add(new Link("关于我们", "/aboutMe"));

        page.setSessionId(session.getId());
        SessionItem si = getSessionItem(session);
        if (si != null) {
            page.setLogin(true);
        }
        return page;
    }

    public ResponseItem check(BindingResult result) {
        return check(result, null, false);
    }

    public boolean checkCaptcha(HttpServletRequest request) {

        String captchaS = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String captchaR = request.getParameter("captcha");
        return StringUtils.equals(captchaS, captchaR);
    }

    public ResponseItem check(BindingResult result, HttpServletRequest request, boolean captcha) {
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        if (captcha && !checkCaptcha(request)) {
            ri.addData("验证码输入不正确");
        }

        for (ObjectError error : result.getAllErrors()) {
            ri.addData(error.getDefaultMessage());
        }
        if (ri.getData().size() == 0) {
            ri.setOk(true);
        }
        return ri;
    }


    @PostConstruct
    void init() {
        sessionKey = stringHelper.random(12);
    }

    private String sessionKey;
    @Resource
    private CacheService cacheService;
    @Resource
    private StringHelper stringHelper;
    @Resource
    private UserService userService;
    @Resource
    private LogService logService;
    @Resource
    private PluginUtil pluginUtil;

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }
}
