package com.odong.server.dao.impl;

import com.odong.server.dao.FriendLinkDao;
import com.odong.server.entity.FriendLink;
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
