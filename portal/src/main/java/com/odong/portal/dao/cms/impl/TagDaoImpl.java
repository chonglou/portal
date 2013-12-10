package com.odong.portal.dao.cms.impl;

import com.odong.portal.dao.cms.TagDao;
import com.odong.portal.dao.impl.BaseJpa2DaoImpl;
import com.odong.portal.entity.cms.Tag;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:33
 */
@Repository("cms.tagDao")
public class TagDaoImpl extends BaseJpa2DaoImpl<Tag, Long> implements TagDao {
}
