package com.odong.portal.dao.impl;

import com.odong.portal.dao.UserDao;
import com.odong.portal.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 上午3:23
 */
@Repository("userDao")
public class UserDaoImpl extends BaseJpa2DaoImpl<User, Long> implements UserDao {

}
