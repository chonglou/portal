package com.odong.portal.dao.impl;

import com.odong.portal.dao.FriendLinkDao;
import com.odong.portal.entity.FriendLink;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午5:40
 */
@Repository("friendLinkDao")
public class FriendLinkDaoImpl extends BaseJpa2DaoImpl<FriendLink, Long> implements FriendLinkDao {
}
