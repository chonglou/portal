package com.odong.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by flamen on 13-12-9.
 */
@Controller("c.oAuth")
@RequestMapping(value = "/video")
public class VideoController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    String getPlayer(Map<String, Object> map, @PathVariable int id){
        map.put("url","");
        map.put("type", "flv");
        map.put("title", "");
        return "player";
    }
}
