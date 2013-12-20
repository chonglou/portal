package com.odong.portal.util;

import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.entity.cms.Article;
import com.odong.portal.entity.cms.Tag;
import com.odong.portal.model.profile.GoogleAuthProfile;
import com.odong.portal.model.profile.QQAuthProfile;
import com.odong.portal.model.profile.SmtpProfile;
import com.odong.portal.web.Card;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.Page;
import com.odong.portal.web.Pager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-5
 * Time: 下午10:19
 */
public interface CacheService {
    GoogleAuthProfile getGoogleAuthProfile();

    void popGoogleAuthProfile();

    QQAuthProfile getQQAuthProfile();

    void popQQAuthProfile();

    String getSiteDomain();

    String getSiteTitle();

    SmtpProfile getSmtp();

    HashMap getShareCodes();

    void popShareCodes();

    Pager getPager();

    Page getUserPage(long user);

    List<Page> getTagPagesByArticle(long article);

    ArrayList getTagPages();

    Article getArticle(long article);

    List<Card> getArticleCardsByPager(int page, int size);

    List<Card> getArticleCardsBySearch(String key);

    List<Card> getArticleCardsByTag(long tag);

    List<Card> getArticleCardsByUser(long user);

    List<Card> getArticleCardByMonth(int year, int month);

    List<Card> getArticleCardByDay(int year, int month, int day);

    ArrayList getLeastArticleCards();

    ArrayList getUserCards();

    long getTopTag();

    Date getStartUp();

    HashMap getSiteInfo();

    NavBar getLatestArticleNavBar();

    NavBar getLeastCommentNavBar();

    NavBar getRecentArchiveNavBar();

    NavBar getTagNavBar();

    NavBar getUserNavBar();

    NavBar getWikiNavBar();

    NavBar getArchiveNavBar();

    User getUser(long user);

    OpenId getOpenId(String openId, OpenId.Type type);

    Tag getTag(long tag);

    ArrayList getLogList();

    String getAboutMe();

    String getGoogleSearch();

    String getGoogleValidCode();

    void popUser(long user);

    void popOpenId(String openId, OpenId.Type type);

    void popAboutMe();

    void popSiteInfo();

    void popDomain();

    void popSiteTitle();

    void popGoogleSearch();

    void popGoogleValidCode();

    void popSmtp();

    void popArticle(long article);

    void popPager();


}
