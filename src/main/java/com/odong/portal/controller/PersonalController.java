package com.odong.portal.controller;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
public class PersonalController {
    void logout(HttpSession session){
        session.invalidate();
    }
}
