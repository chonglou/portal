package com.odong.portal.dao.impl;

import com.odong.portal.dao.OpenIdDao;
import com.odong.portal.entity.OpenId;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-11-12
 * Time: 下午11:59
 */
@Repository("openIdDao")
public class OpenIdDaoImpl extends BaseJpa2DaoImpl<OpenId, Long> implements OpenIdDao {
}
