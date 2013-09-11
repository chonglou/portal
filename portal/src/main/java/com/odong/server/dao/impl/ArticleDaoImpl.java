package com.odong.server.dao.impl;

import com.odong.server.dao.ArticleDao;
import com.odong.server.entity.Article;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("articleDao")
public class ArticleDaoImpl extends BaseJpa2DaoImpl<Article, String> implements ArticleDao {
}
