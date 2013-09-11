package com.odong.server.dao.rbac.impl;

import com.odong.server.dao.impl.BaseJpa2DaoImpl;
import com.odong.server.dao.rbac.PermissionDao;
import com.odong.server.entity.rbac.Permission;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:29
 */
@Repository("rbac.permissionDao")
public class PermissionDaoImpl extends BaseJpa2DaoImpl<Permission, Long> implements PermissionDao {
}
