package com.odong.portal.util;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
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

    NavBar getArchiveNavBar();

    User getUser(long user);

    Tag getTag(long tag);

    ArrayList getLogList();

    String getAboutMe();

    String getGoogleSearch();

    String getGoogleValidCode();

    void popAboutMe();

    void popSiteInfo();

    void popDomain();

    void popGoogleSearch();

    void popGoogleValidCode();

    void popSmtp();

    void popArticle(long article);

    void popPager();


}
