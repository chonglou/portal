package com.odong.server.dao.impl;

import com.odong.server.dao.ArticleTagDao;
import com.odong.server.entity.ArticleTag;
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
