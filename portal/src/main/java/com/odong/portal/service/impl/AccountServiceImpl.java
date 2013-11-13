package com.odong.portal.service.impl;

import com.odong.portal.dao.OpenIdDao;
import com.odong.portal.dao.UserDao;
import com.odong.portal.entity.OpenId;
import com.odong.portal.entity.User;
import com.odong.portal.model.Contact;
import com.odong.portal.service.AccountService;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:38
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Override
    public OpenId getOpenId(String openId, OpenId.Type type) {
        Map<String, Object> map = new HashMap<>();
        map.put("oid", openId);
        map.put("type", type);
        return openIdDao.select("FROM OpenId  i WHERE i.oid=:oid AND i.type=:type", map);
    }

    @Override
    public long addQQUser(String openId, String accessToken, String nickname) {
        User u = new User();
        u.setEmail(stringHelper.random(8)+"@localhost");
        u.setUsername(nickname);
        u.setPassword(encryptHelper.encrypt(stringHelper.random(12)));
        u.setCreated(new Date());
        u.setState(User.State.ENABLE);
        long uid= userDao.persist(u);

        OpenId oi = new OpenId();
        oi.setOid(openId);
        oi.setToken(accessToken);
        oi.setType(OpenId.Type.QQ);
        oi.setUser(uid);
        oi.setCreated(new Date());
        openIdDao.insert(oi);

        return uid;  //
    }

    @Override
    public List<User> listUser() {
        return userDao.list();
    }

    @Override
    public void setUserLastLogin(long user) {
        User u = userDao.select(user);
        u.setLastLogin(new Date());
        userDao.update(u);
    }

    @Override
    public void setUserEmail(long user, String email) {
        if (getUser(email) != null) {
            throw new IllegalArgumentException("邮箱[" + email + "]已存在");
        }
        User u = userDao.select(user);
        u.setEmail(email);
        userDao.update(u);
    }

    @Override
    public User getUser(long id) {
        return userDao.select(id);
    }

    @Override
    public User getUser(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        return userDao.select("FROM User  i WHERE i.email=:email", map);
    }

    @Override
    public long addUser(String email, String username, String password) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(encryptHelper.encrypt(password));
        u.setCreated(new Date());
        u.setState(User.State.SUBMIT);
        return userDao.persist(u);
    }

    @Override
    public void setUserContact(long user, Contact contact) {
        User u = userDao.select(user);
        u.setContact(jsonHelper.object2json(contact));
        userDao.update(u);
    }

    @Override
    public void setUserName(long user, String username) {
        User u = userDao.select(user);
        u.setUsername(username);
        userDao.update(u);
    }



    @Override
    public void setUserPassword(long user, String password) {
        User u = userDao.select(user);
        u.setPassword(encryptHelper.encrypt(password));
        userDao.update(u);
    }


    @Override
    public void setUserState(long user, User.State state) {
        User u = userDao.select(user);
        u.setState(state);
        userDao.update(u);
    }

    @Override
    public User auth(String email, String password) {
        User u = getUser(email);
        return u != null && encryptHelper.check(password, u.getPassword()) ? u : null;  //
    }


    @Resource
    private UserDao userDao;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private OpenIdDao openIdDao;
    @Resource
    private StringHelper stringHelper;
    @Resource
    private JsonHelper jsonHelper;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setOpenIdDao(OpenIdDao openIdDao) {
        this.openIdDao = openIdDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }
}
