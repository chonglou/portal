package com.odong.portal.wiki;

import com.odong.portal.wiki.model.WikiPage;

import java.util.Map;

/**
 * Created by flamen on 13-12-19下午8:30.
 */
public interface WikiHelper {
    Map<String, String> listPage();

    WikiPage getPage(String name);

    void setPage(String name, String title, String body);

    void delPage(String name);
}
