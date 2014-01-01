package com.odong.cms.service;

import com.odong.cms.entity.FriendLink;

import java.util.List;

/**
 * Created by flamen on 13-12-31下午7:48.
 */
public interface FriendLinkService {
    void addFriendLink(String name, String url, String logo);

    void setFriendLink(long id, String name, String url, String logo);

    List<FriendLink> listFriendLink();

    FriendLink getFriendLink(long id);

}
