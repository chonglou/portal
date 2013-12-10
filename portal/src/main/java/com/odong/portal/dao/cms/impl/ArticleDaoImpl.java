package com.odong.portal.dao.cms.impl;

import com.odong.portal.dao.cms.ArticleDao;
import com.odong.portal.dao.impl.BaseJpa2DaoImpl;
import com.odong.portal.entity.cms.Article;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("cms.articleDao")
public class ArticleDaoImpl extends BaseJpa2DaoImpl<Article, Long> implements ArticleDao {

}
