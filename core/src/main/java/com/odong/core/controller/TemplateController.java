package com.odong.core.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by flamen on 14-1-1下午11:06.
 */
@Controller("core.c.template")
@RequestMapping(value = "/templates/core", method = RequestMethod.GET)
public class TemplateController {
    @RequestMapping(value = "/base.httl")
    @ResponseBody
    ClassPathResource getBase(){
        return new ClassPathResource("/templates/httl/core/base.httl");
    }
}
