package com.odong.portal.dao.impl;

import com.odong.portal.dao.WebSiteDao;
import com.odong.portal.entity.WebSite;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 13-12-20上午1:46.
 */
@Repository("webSiteDao")
public class WebSiteDaoImpl extends BaseJpa2DaoImpl<WebSite,Long> implements WebSiteDao {
}
