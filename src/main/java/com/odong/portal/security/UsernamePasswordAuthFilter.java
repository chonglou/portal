package com.odong.portal.security;

import com.odong.portal.entity.User;
import com.odong.portal.service.AccountService;
import com.odong.portal.util.FormHelper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-13
 * Time: 下午1:26
 */
public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("不支持的验证method:" + request.getMethod());
        }

        if (!formHelper.checkCaptcha(request)) {
            throw new AuthenticationServiceException("验证码输入不正确");
        }


        String username = obtainUsername(request);
        String password = obtainPassword(request);
        User user = accountService.auth(username, password);
        if (user == null) {
            throw new AuthenticationServiceException("用户名密码不匹配");
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authRequest);


    }


    @Override
    protected String obtainPassword(HttpServletRequest request) {
        Object obj = request.getParameter("password");
        return obj == null ? "" : obj.toString();
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        Object obj = request.getParameter("username");
        return obj == null ? "" : obj.toString();
    }

    private FormHelper formHelper;
    private AccountService accountService;

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
