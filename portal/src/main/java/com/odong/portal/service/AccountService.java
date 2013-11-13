package com.odong.portal.service;

import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.model.Contact;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
public interface AccountService {

    void setOpenIdToken(long openId, String token);
    OpenId getOpenId(String openId, OpenId.Type type);

    long addQQUser(String openId, String accessToken, String nickname);

    List<User> listUser();

    void setUserLastLogin(long user);

    void setUserEmail(long user, String email);

    User getUser(long id);

    User getUser(String email);

    long addUser(String email, String username, String password);

    void setUserContact(long user, Contact contact);
    void setUserName(long user, String username);

    //void setUserInfo(long user, String username, String contact);

    void setUserPassword(long user, String password);

    void setUserState(long user, User.State state);

    User auth(String email, String password);

}
