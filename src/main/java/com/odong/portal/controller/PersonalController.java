package com.odong.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller
@RequestMapping(value = "/personal")
public class PersonalController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    String getLogin() {
        return "aaa";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    void logout(HttpSession session) {
        session.invalidate();
    }
}
