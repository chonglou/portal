package com.odong.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 13-12-10 下午1:48.
 */
public class Feed implements Serializable {
    public enum Type {
        VIDEO, BOOK
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Item item : items) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Item.NAME, item.getName());
            map.put(Item.DETAILS, item.getDetails());
            list.add(map);
        }
        return list;
    }

    public int getSize() {
        return items.size();
    }

    public Feed(Type type) {
        items = new ArrayList<Item>();
        this.type = type;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItem(int i) {
        return items.get(i);
    }

    private static final long serialVersionUID = 4358226567718154525L;

    private List<Item> items;
    private Type type;

    public Type getType() {
        return type;
    }

}
