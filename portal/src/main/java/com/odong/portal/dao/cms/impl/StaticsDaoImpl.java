package com.odong.portal.dao.cms.impl;

import com.odong.portal.dao.cms.StaticsDao;
import com.odong.portal.dao.impl.BaseJpa2DaoImpl;
import com.odong.portal.entity.cms.Statics;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 13-12-10 上午10:04.
 */
@Repository("cms.staticsDao")
public class StaticsDaoImpl extends BaseJpa2DaoImpl<Statics, Long> implements StaticsDao {
}
