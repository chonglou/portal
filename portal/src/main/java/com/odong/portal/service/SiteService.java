package com.odong.portal.service;

import com.odong.portal.entity.FriendLink;
import com.odong.portal.entity.Setting;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
public interface SiteService {
    FriendLink getFriendLink(long id);

    void addFriendLink(String name, String url, String logo);

    void setFriendLink(long id, String name, String url, String logo);

    List<FriendLink> listFriendLink();

    List<Setting> listSetting();

    void delFriendLink(long id);

    Boolean getBoolean(String key);

    Date getDate(String key);

    Long getLong(String key);

    Integer getInteger(String key);

    String getString(String key);

    void set(String key, Object value);

    <T> T getObject(String key, Class<T> clazz);

    <T> List<T> getList(String key, Class<T> clazz);
}
