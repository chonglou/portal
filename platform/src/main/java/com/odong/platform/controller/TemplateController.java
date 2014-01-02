package com.odong.platform.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by flamen on 14-1-1下午11:09.
 */
@Controller("platform.c.template")
@RequestMapping(value = "/templates/platform", method = RequestMethod.GET)
public class TemplateController {
    @RequestMapping(value = "/install.httl")
    @ResponseBody
    ClassPathResource getBase(){
        return new ClassPathResource("/templates/httl/platform/install.httl");
    }
}
