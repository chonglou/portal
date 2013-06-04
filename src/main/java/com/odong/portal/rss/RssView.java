package com.odong.portal.rss;

import com.odong.portal.service.SiteService;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 12-5-16
 * Time: 下午8:53
 */

public class RssView extends AbstractRssFeedView {
    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        @SuppressWarnings("unchecked")
        List<RssContent> listContent = (List<RssContent>) model.get("feedContent");
        List<Item> items = new ArrayList<>(listContent.size());
        for (RssContent c : listContent) {
            Item i = new Item();

            Content content = new Content();
            content.setValue(c.getSummary());
            i.setContent(content);
            if(c.getAuthor() != null){
                i.setAuthor(c.getAuthor());
            }
            i.setTitle(c.getTitle());
            i.setLink(c.getUrl());
            i.setPubDate(c.getPublish());

            items.add(i);
        }
        return items;  //
    }

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
        feed.setTitle((String)model.get("feedTitle"));
        feed.setDescription((String)model.get("feedDesc"));
        feed.setLink((String)model.get("feedLink"));
        super.buildFeedMetadata(model, feed, request);
    }

}
