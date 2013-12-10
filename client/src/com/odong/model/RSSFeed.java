package com.odong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-9.
 */
public class RSSFeed implements Serializable {
    public RSSFeed() {
        items = new ArrayList<RSSItem>();
    }

    public void addItem(RSSItem item) {
        items.add(item);
    }

    public int getSize() {
        return items.size();
    }

    public RSSItem getItem(int i) {
        return items.get(i);
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (RSSItem item : items) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(RSSItem.TITLE, item.getTitle());
            map.put(RSSItem.PUB_DATE, item.getPubDate());
            list.add(map);
        }
        return list;
    }


    private static final long serialVersionUID = -193370275854289739L;
    private String title;
    private String pubDate;
    private List<RSSItem> items;

    public List<RSSItem> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
