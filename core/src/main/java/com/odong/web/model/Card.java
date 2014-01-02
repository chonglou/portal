package com.odong.web.model;

import org.jsoup.Jsoup;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-12
 * Time: 下午12:13
 */
public class Card implements Serializable {
    public Card(String logo, String title, String body, String url) {
        this.logo = logo;
        this.title = title;
        this.url = url;
        this.body = Jsoup.parse(body).body().text();
    }

    @Deprecated
    public Card() {
    }

    private static final long serialVersionUID = 1863610252038450249L;
    private String logo;
    private String title;
    private String body;
    private String url;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
