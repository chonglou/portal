package com.odong.portal.wiki;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by flamen on 13-12-19下午8:30.
 */
public interface WikiHelper {
    Map<String,String> listPage();

    Page getPage(String name);
    void setPage(String name, String title, String body);

    void delPage(String name);
}
