package com.odong.portal.dao.cms.impl;

import com.odong.portal.dao.cms.ArticleTagDao;
import com.odong.portal.dao.impl.BaseJpa2DaoImpl;
import com.odong.portal.entity.cms.ArticleTag;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("cms.articleTagDao")
public class ArticleTagDaoImpl extends BaseJpa2DaoImpl<ArticleTag, Long> implements ArticleTagDao {
}
