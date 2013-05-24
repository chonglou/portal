package com.odong.portal.dao.impl;

import com.odong.portal.dao.TagDao;
import com.odong.portal.entity.Tag;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("tagDao")
public class TagDaoImpl extends BaseJpa2DaoImpl<Tag, Long> implements TagDao {
}
