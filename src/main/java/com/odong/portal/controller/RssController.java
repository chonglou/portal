package com.odong.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午11:59
 */
@Controller
public class RssController {
    @RequestMapping(value = "/rss.xml", method = RequestMethod.GET)
    ModelAndView getRss() {
        return null;
    }
}
