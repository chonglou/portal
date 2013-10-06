package com.odong.portal.util.impl;

import com.odong.portal.entity.Article;
import com.odong.portal.entity.FriendLink;
import com.odong.portal.entity.Tag;
import com.odong.portal.entity.User;
import com.odong.portal.service.AccountService;
import com.odong.portal.util.CacheService;
import com.odong.portal.service.ContentService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.web.Card;
import com.odong.portal.web.NavBar;
import com.odong.portal.web.Page;
import com.odong.portal.web.Pager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-10-5
 * Time: 下午10:19
 */
@Component
public class CacheServiceImpl implements CacheService {
    @Override
    public Pager getPager() {
        return cacheHelper.get("pager", Pager.class, null, () -> {
            int size = siteService.getInteger("site.articlePageSize");
            long total = contentService.countArticle();
            int count = (int) (total / size);
            if (total % size != 0) {
                count++;
            }
            return new Pager("/page", size, 1, count);
        });
    }


    @Override
    public Page getUserPage(long user) {
        return cacheHelper.get("page/user/" + user, Page.class, null, () -> {
            User u = accountService.getUser(user);
            return new Page(u.getUsername(), "/user/" + u.getId());
        });
    }

    @Override
    public List<Page> getTagPagesByArticle(long article) {
        HashSet ids = cacheHelper.get("article/" + article + "/tags", HashSet.class, null, () -> (HashSet) contentService.getTagIdsByArticle(article));
        List<Page> pages = new ArrayList<>();
        for (Object obj : ids) {
            pages.add(getTag((Long) obj).toPage());
        }
        return pages;
    }

    @Override
    public ArrayList getTagPages() {
        return cacheHelper.get("pages/tag", ArrayList.class, null, () -> {
            ArrayList<Page> pages = new ArrayList<>();
            contentService.listTag().forEach((t) -> pages.add(t.toPage()));
            return pages;
        });
    }

    @Override
    public Article getArticle(long id) {
        return cacheHelper.get("article/" + id, Article.class, null, () -> contentService.getArticle(id));  //
    }

    @Override
    public List<Card> getArticleCardsBySearch(String key) {
        return getCards(cacheHelper.get(
                "search/" + key,
                ArrayList.class,
                null, () -> (ArrayList) contentService.getArticleIdsBySearch(key)));
    }

    @Override
    public User getUser(long id) {
        return cacheHelper.get("user/" + id, User.class, null, () -> accountService.getUser(id));
    }


    @Override
    public List<Card> getArticleCardsByPager(int no, int size) {
        return getCards(cacheHelper.get("cards/article/pager/" + no, ArrayList.class, null, () -> (ArrayList) contentService.getArticleIdsByPage(no, size)));  //
    }

    @Override
    public List<Card> getArticleCardsByTag(long id) {
        return getCards(cacheHelper.get("cards/article/tag/" + id, HashSet.class, null, () -> (HashSet) contentService.getArticleIdsByTag(id)));
    }

    @Override
    public List<Card> getArticleCardsByUser(long id) {
        return getCards(cacheHelper.get("cards/article/user/" + id, ArrayList.class, null, () -> (ArrayList) contentService.getArticleIdsByUser(id)));
    }

    @Override
    public List<Card> getArticleCardByMonth(int year, int month) {
        return getCards(
                cacheHelper.get("cards/article/archive/" + year + "-" + month,
                        ArrayList.class,
                        null,
                        () -> (ArrayList) contentService.getArticleIdsByMonth(year, month))
        );
    }

    @Override
    public ArrayList getLeastArticleCards() {
        return cacheHelper.get(
                "cards/article/least",
                ArrayList.class,
                null,
                () -> {
                    ArrayList<Card> cards = new ArrayList<>();
                    contentService.hotArticle(siteService.getInteger("site.articlePageSize")).forEach((a) -> cards.add(a.toCard()));
                    return cards;
                }
        );
    }

    @Override
    public ArrayList getUserCards() {
        return cacheHelper.get("cards/user", ArrayList.class, null, () -> {
            ArrayList<Card> cards = new ArrayList<>();
            accountService.listUser().forEach((u) -> {
                if (u.getState() == User.State.ENABLE && !u.getEmail().equals(manager)) {
                    cards.add(new Card(u.getLogo(), u.getUsername(), u.getEmail(), "/user/" + u.getId()));
                }
            });
            return cards;
        });
    }

    @Override
    public long getTopTag() {
        return cacheHelper.get("tag/top", Long.class, null, () -> siteService.getLong("site.topTag"));
    }

    @Override
    public Date getStartUp() {
        return cacheHelper.get("startup", Date.class, null, () -> siteService.getObject("site.startup", Date.class));
    }

    @Override
    public HashMap getSiteInfo() {
        return cacheHelper.get("site/info", HashMap.class, null,
                () -> {
                    HashMap<String, Object> site = new HashMap<>();
                    for (String s : new String[]{"title", "description", "copyright", "keywords", "author", "articlePageSize"}) {
                        site.put(s, siteService.getString("site." + s));
                    }

                    Map<String, String> topNavs = new HashMap<>();
                    topNavs.put("main", "站点首页");
                    topNavs.put("personal/self", "用户中心");
                    topNavs.put("sitemap", "网站地图");
                    topNavs.put("aboutMe", "关于我们");
                    site.put("topNavs", topNavs);

                    List<Page> tagCloud = new ArrayList<>();
                    for (Tag tag : contentService.hotTag(siteService.getInteger("site.hotTagCount"))) {
                        tagCloud.add(new Page(tag.getName(), "/tag/" + tag.getId()));
                    }
                    for (FriendLink fl : siteService.listFriendLink()) {
                        tagCloud.add(new Page(fl.getName(), fl.getUrl()));
                    }
                    site.put("tagCloud", tagCloud);
                    site.put("advertLeft", siteService.getString("site.advert.left"));
                    site.put("advertBottom", siteService.getString("site.advert.bottom"));

                    site.put("manager", manager);
                    return site;
                }
        );
    }

    @Override
    public NavBar getLatestArticleNavBar() {
        return cacheHelper.get("navBar/latestArticle", NavBar.class, null, () -> {
            NavBar nb = new NavBar("最新文章");
            nb.setType(NavBar.Type.LIST);
            contentService.latestArticle(siteService.getInteger("site.latestArticleCount")).forEach((a) -> nb.add(a.getTitle(), "/article/" + a.getId()));

            return nb;
        });
    }

    @Override
    public NavBar getLeastCommentNavBar() {
        return cacheHelper.get("navBar/leastComment", NavBar.class, null, () -> {
            NavBar nb = new NavBar("最新评论");
            nb.setType(NavBar.Type.LIST);
            contentService.latestComment(siteService.getInteger("site.latestCommentCount")).forEach((c) -> nb.add(c.getContent(), "/article/" + c.getArticle() + "#c" + c.getId()));
            return nb;
        });
    }

    @Override
    public NavBar getRecentArchiveNavBar() {
        return cacheHelper.get("navBar/recentArchive", NavBar.class, null, () -> {
            NavBar nb = new NavBar("最近归档");
            nb.setType(NavBar.Type.LIST);
            DateTime init = new DateTime(siteService.getDate("site.init")).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            for (int i = 0; i < siteService.getInteger("site.archiveCount"); i++) {
                DateTime dt = new DateTime().plusMonths(0 - i);
                if (dt.compareTo(init) >= 0) {
                    addArchive2NavBar(nb, dt);
                } else {
                    break;
                }
            }
            return nb;
        });  //
    }

    @Override
    public NavBar getTagNavBar() {
        return cacheHelper.get("navBar/tags", NavBar.class, null, () -> {
            NavBar nb = new NavBar("标签列表");
            nb.setType(NavBar.Type.LIST);
            for (Tag t : contentService.listTag()) {
                nb.add(t.getName(), "/tag/" + t.getId());
            }
            return nb;
        });

    }

    @Override
    public NavBar getUserNavBar() {
        return cacheHelper.get("navBar/users", NavBar.class, null, () -> {
            NavBar nb = new NavBar("用户列表");
            nb.setType(NavBar.Type.LIST);
            for (User u : accountService.listUser()) {
                if (u.getState() == User.State.ENABLE && !u.getEmail().equals(manager)) {
                    nb.add(u.getUsername(), "/user/" + u.getId());
                }
            }
            return nb;
        });
    }

    @Override
    public NavBar getArchiveNavBar() {
        return cacheHelper.get("navBar/archives", NavBar.class, null, () -> {

            NavBar nb = new NavBar("归档列表");
            nb.setType(NavBar.Type.LIST);
            DateTime now = new DateTime();

            for (DateTime init = new DateTime(siteService.getDate("site.init")).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
                 init.compareTo(now) <= 0;
                 init = init.plusMonths(1)) {
                addArchive2NavBar(nb, init);
            }
            return nb;
        });
    }

    @Override
    public Tag getTag(long id) {
        return cacheHelper.get("tag/" + id, Tag.class, null, () -> contentService.getTag(id));
    }

    @Override
    public ArrayList getLogList() {
        return cacheHelper.get("logs", ArrayList.class, null, () -> {
            ArrayList<String> logList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/Change-Logs")))) {
                String line;
                while ((line = br.readLine()) != null) {
                    logList.add(line);
                }
            } catch (IOException e) {
                logger.error("加载大事记文件出错", e);
            }
            return logList;
        });
    }

    @Override
    public String getAboutMe() {
        return cacheHelper.get("aboutMe", String.class, null, () -> siteService.getString("site.aboutMe"));
    }

    @Override
    public String getGoogleSearch() {
        return cacheHelper.get("site/google/search", String.class, null, () -> siteService.getString("site.google.search"));
    }

    @Override
    public String getGoogleValidCode() {
        return cacheHelper.get("site/google/valid", String.class, null, () -> siteService.getString("site.google.valid"));  //
    }

    @Override
    public void popAboutMe() {
        cacheHelper.delete("site.aboutMe");
    }

    @Override
    public void popSiteInfo() {
        cacheHelper.delete("site/info");
    }

    @Override
    public void popDomain() {
        cacheHelper.delete("site/domain");
    }

    @Override
    public void popGoogleSearch() {
        cacheHelper.delete("site/google/search");

    }

    @Override
    public void popSmtp() {
        cacheHelper.delete("site/smtp");
    }

    @Override
    public void popArticle(long article) {
        cacheHelper.delete("article/" + article);
    }

    @Override
    public void popPager() {
        cacheHelper.delete("paper");
    }


    @Override
    public void popGoogleValidCode() {
        cacheHelper.delete("site/google/valid");
    }

    private void addArchive2NavBar(NavBar nb, DateTime dt) {
        nb.add(dt.toString("yyyy年MM月"), "/archive/" + dt.toString("yyyy-MM"));
    }


    private List<Card> getCards(Collection ids) {
        List<Card> cards = new ArrayList<>();
        for (Object obj : ids) {
            cards.add(getArticle((Long) obj).toCard());
        }
        return cards;
    }


    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private ContentService contentService;
    @Resource
    private AccountService accountService;
    @Value("${app.manager}")
    protected String manager;
    private final static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }


    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }
}
