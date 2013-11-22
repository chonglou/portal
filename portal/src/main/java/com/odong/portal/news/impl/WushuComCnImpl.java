package com.odong.portal.news.impl;

import com.odong.portal.news.Crawler;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-18
 * Time: 下午1:14
 */
public class WushuComCnImpl implements Crawler {
    @Override
    public String name() {
        return "http://www.wushu.com.cn";  //
    }
}
