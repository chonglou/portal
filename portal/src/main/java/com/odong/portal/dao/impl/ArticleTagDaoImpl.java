package com.odong.portal.dao.impl;

import com.odong.portal.dao.ArticleTagDao;
import com.odong.portal.entity.ArticleTag;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("articleTagDao")
public class ArticleTagDaoImpl extends BaseJpa2DaoImpl<ArticleTag, Long> implements ArticleTagDao {
}
