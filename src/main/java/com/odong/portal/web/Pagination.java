package com.odong.portal.web;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-3
 * Time: 下午7:16
 */
public class Pagination implements Serializable {
    private static final long serialVersionUID = -1465901355572460271L;
    private int total;
    private int pages;
    private String url;
}
