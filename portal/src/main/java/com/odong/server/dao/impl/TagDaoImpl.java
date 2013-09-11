package com.odong.server.dao.impl;

import com.odong.server.dao.TagDao;
import com.odong.server.entity.Tag;
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
