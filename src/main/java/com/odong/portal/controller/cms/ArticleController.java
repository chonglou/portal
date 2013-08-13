package com.odong.portal.controller.cms;

import com.odong.portal.model.SessionItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */
@Controller("c.cms.article")
@RequestMapping(value = "/article")
@SessionAttributes(SessionItem.KEY)
public class ArticleController {
}
