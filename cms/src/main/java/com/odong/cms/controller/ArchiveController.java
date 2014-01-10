package com.odong.cms.controller;

import com.odong.portal.controller.PageController;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午3:09
 */
@Controller("c.cms.archive")
@RequestMapping(value = "/archive")
@SessionAttributes(SessionItem.KEY)
public class ArchiveController extends PageController {


}
