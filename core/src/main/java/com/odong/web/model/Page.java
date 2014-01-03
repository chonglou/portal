package com.odong.web.model;

import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午2:22.
 */
public class Page implements Serializable {
    public Page() {
        this.topLinks = new ArrayList<>();
        this.tagCloud = new ArrayList<>();
        this.sideBars = new ArrayList<>();
        this.personalBar = new ArrayList<>();
    }

    private static final long serialVersionUID = 2111659422383723732L;
    private String index;
    private String domain;
    private String title;
    private String author;
    private String description;
    private String keywords;
    private String copyright;
    private String hAd;
    private String vAd;
    private String calendar;
    private List<Link> topLinks;
    private List<Link> tagCloud;
    private List<Link> personalBar;
    private List<SideBar> sideBars;
    private QqAuthProfile qqAuth;
    private GoogleAuthProfile googleAuth;
    private boolean debug;
    private boolean login;
    private String sessionId;
    private String userLogo;
    private String userName;
    private String captcha;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<Link> getPersonalBar() {
        return personalBar;
    }

    public void setPersonalBar(List<Link> personalBar) {
        this.personalBar = personalBar;
    }

    public List<SideBar> getSideBars() {
        return sideBars;
    }

    public void setSideBars(List<SideBar> sideBars) {
        this.sideBars = sideBars;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String gethAd() {
        return hAd;
    }

    public void sethAd(String hAd) {
        this.hAd = hAd;
    }

    public String getvAd() {
        return vAd;
    }

    public void setvAd(String vAd) {
        this.vAd = vAd;
    }

    public List<Link> getTagCloud() {
        return tagCloud;
    }

    public void setTagCloud(List<Link> tagCloud) {
        this.tagCloud = tagCloud;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<Link> getTopLinks() {
        return topLinks;
    }

    public void setTopLinks(List<Link> topLinks) {
        this.topLinks = topLinks;
    }

    public QqAuthProfile getQqAuth() {
        return qqAuth;
    }

    public void setQqAuth(QqAuthProfile qqAuth) {
        this.qqAuth = qqAuth;
    }

    public GoogleAuthProfile getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuthProfile googleAuth) {
        this.googleAuth = googleAuth;
    }
}
