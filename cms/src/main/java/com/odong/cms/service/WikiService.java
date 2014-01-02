package com.odong.cms.service;

import com.odong.cms.entity.Wiki;
import com.odong.web.model.Link;

import java.util.List;

/**
 * Created by flamen on 13-12-31下午7:48.
 */
public interface WikiService {
    Wiki getWiki(String name);

    List<Link> listWiki();

    void setWiki(long user, String name, String title, String body, int version);
}
