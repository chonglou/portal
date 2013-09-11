package com.odong.server.dao.rbac.impl;

import com.odong.server.dao.impl.BaseJpa2DaoImpl;
import com.odong.server.dao.rbac.OperationDao;
import com.odong.server.entity.rbac.Operation;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:29
 */
@Repository("rbac.operationDao")
public class OperationDaoImpl extends BaseJpa2DaoImpl<Operation, Long> implements OperationDao {
}
