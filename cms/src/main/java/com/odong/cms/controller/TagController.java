package com.odong.cms.controller;

import com.odong.portal.controller.PageController;
import com.odong.portal.entity.cms.Tag;
import com.odong.portal.job.TaskSender;
import com.odong.portal.model.SessionItem;
import com.odong.portal.web.NavBar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:22
 */
@Controller("c.cms.tag")
@RequestMapping(value = "/tag")
public class TagController  {
    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    String getTag(Map<String, Object> map, @PathVariable long tagId, HttpServletResponse response) throws IOException {

        Tag tag = cacheService.getTag(tagId);
        if (tag != null) {
            taskSender.visitTag(tagId);
            List<NavBar> navBars = new ArrayList<>();

            navBars.add(cacheService.getTagNavBar());
            map.put("navBars", navBars);

            map.put("articleList", cacheService.getArticleCardsByTag(tagId));
            fillSiteInfo(map);
            map.put("title", "标签-[" + tag.getName() + "]");
            map.put("tag", tag);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return "cms/tag";
    }

    @Resource
    private TaskSender taskSender;

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }
}
