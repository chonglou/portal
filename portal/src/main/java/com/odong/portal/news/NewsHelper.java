package com.odong.portal.news;

import com.odong.portal.web.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-20上午1:51.
 */
public interface NewsHelper {
    void add(String title, String body, String source, String url);
    void set(int index,String title, String body, String source, String url);

    int count();
    List<Page> page(int start, int stop);
}
