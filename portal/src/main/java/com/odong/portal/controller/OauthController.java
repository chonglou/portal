package com.odong.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午5:44
 */

@Controller("c.oAuth")
public class OauthController {
    @RequestMapping(value = "/oauth/qq", method = RequestMethod.GET)
    @ResponseBody
    void qqAuth(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.getWriter().write("test");
    }
}
