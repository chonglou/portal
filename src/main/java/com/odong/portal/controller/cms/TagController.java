package com.odong.portal.controller.cms;

import com.odong.portal.model.SessionItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:22
 */
@Controller("c.cms.tag")
@RequestMapping(value = "/tag")
@SessionAttributes(SessionItem.KEY)
public class TagController {
}
