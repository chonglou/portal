package com.odong.server.dao.rbac.impl;

import com.odong.server.dao.impl.BaseJpa2DaoImpl;
import com.odong.server.dao.rbac.ResourceDao;
import com.odong.server.entity.rbac.Resource;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:29
 */
@Repository("rbac.resourceDao")
public class ResourceDaoImpl extends BaseJpa2DaoImpl<Resource, Long> implements ResourceDao {
}
