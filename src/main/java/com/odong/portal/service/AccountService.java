package com.odong.portal.service;

import com.odong.portal.entity.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
public interface AccountService {

    List<User> listUser();

    void setUserEmail(long user, String email);

    User getUser(long id);

    User getUser(String email);

    void addUser(String email, String username, String password);

    void setUserInfo(long user, String username, String details);

    void setUserPassword(long user, String password);

    void setUserState(long user, User.State state);

    User auth(String email, String password);

}
