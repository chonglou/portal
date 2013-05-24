package com.odong.portal.dao.impl;

import com.odong.portal.dao.UserDao;
import com.odong.portal.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 上午3:23
 */
@Repository("userDao")
public class UserDaoImpl extends BaseJpa2DaoImpl<User, Long> implements UserDao {
    @Override
    public User select(String email) {

        try {
            return entityManager.createQuery("SELECT i FROM " + tableName() + " AS i WHERE i.email=:email", User.class).setParameter("email", email).getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
