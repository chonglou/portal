package com.odong.core.service;

import com.odong.core.entity.User;
import com.odong.core.model.Contact;

import java.util.List;

/**
 * Created by flamen on 13-12-30下午3:50.
 */
public interface UserService {
    void addGoogleUser(String openId, String token);

    long addQqUser(String openId, String accessToken, String username);

    List<User> listUser();

    void setUserLastLogin(long user);

    void setUserEmail(long user, String email);

    User getUser(long id);

    void addUser(String email, String username, String password);

    void setUserContact(long user, Contact contact);

    void setUserName(long user, String username);

    void setUserPassword(long user, String password);

    void setUserState(long user, User.State state);

    boolean auth(String email, String password);
}
