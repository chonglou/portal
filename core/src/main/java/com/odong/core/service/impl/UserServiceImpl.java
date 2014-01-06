package com.odong.core.service.impl;

import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.entity.User;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.Contact;
import com.odong.core.service.UserService;
import com.odong.core.store.JdbcHelper;
import com.odong.core.util.StringHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 13-12-31下午3:22.
 */
@Component("core.userService")
public class UserServiceImpl extends JdbcHelper implements UserService {

    @Override
    public User getUser(String openId, User.Type type) {
        return select(SELECT + "WHERE openId_=? AND type_=?", new Object[]{openId, type.name()}, mapperUser());
    }

    @Override
    public long addGoogleUser(String openId, String token) {
        String username = stringHelper.random(8);
        return addUser(openId, User.Type.GOOGLE, username + "@localhost", username, stringHelper.random(12), token);
    }

    @Override
    public long addQqUser(String openId, String accessToken, String username) {
        return addUser(openId, User.Type.QQ, stringHelper.random(8) + "@localhost", username, stringHelper.random(12), accessToken);
    }

    @Override
    public List<User> listUser() {
        return list(SELECT, mapperUser());
    }

    @Override
    public void setUserLastLogin(long user) {
        execute("UPDATE Users SET lastLogin_=?,version=version+1 WHERE id=?", new Date(), user);
    }

    @Override
    public void setUserEmail(long user, String email) {
        execute("UPDATE Users SET email_=?,version=version+1 WHERE id=?", email, user);
    }

    @Override
    public User getUser(long id) {
        return select(SELECT + "WHERE id=?", new Object[]{id}, mapperUser());
    }

    @Override
    public String getUserContact(long user) {
        return select("SELECT contact_ FROM Users WHERE id=?", new Object[]{user}, String.class);
    }

    @Override
    public long addEmailUser(String email, String username, String password) {
        return addUser(email, User.Type.EMAIL, email, username, password, null);
    }


    @Override
    public void setUserContact(long user, Contact contact) {
        execute("UPDATE Users SET contact_=?,version=version+1 WHERE id=?", jsonHelper.object2json(contact), user);
    }

    @Override
    public void setUserName(long user, String username) {
        execute("UPDATE Users SET username_=?,version=version+1 WHERE id=?", username, user);
    }

    @Override
    public void setUserPassword(long user, String password) {
        execute("UPDATE Users SET password_=?,version=version+1 WHERE id=?", encryptHelper.encrypt(password), user);
    }

    @Override
    public void setUserState(long user, User.State state) {
        execute("UPDATE Users SET state_=?,version=version+1 WHERE id=?", state.name(), user);
    }

    @Override
    public void setUserExtra(long user, String extra) {
        execute("UPDATE Users SET extra_=?,version=version+1 WHERE id=?", extra, user);
    }

    @Override
    public boolean auth(String email, String password) {
        String pwd = select("SELECT password_ FROM Users WHERE email_=? AND type_=?", new Object[]{email, User.Type.EMAIL.name()}, String.class);
        return pwd != null && encryptHelper.check(password, pwd);
    }

    @PostConstruct
    void init() {
        install("Users",
                longIdColumn(),
                stringColumn("openId_", 255, true, true),
                stringColumn("email_", 255, true, true),
                stringColumn("username_", 255, true, false),
                stringColumn("password_", 512, true, false),
                stringColumn("logo_", 255, false, false),
                textColumn("extra_", false),
                textColumn("contact_", true),
                enumColumn("type_"),
                enumColumn("state_"),
                longColumn("visits_", true),
                dateColumn("lastLogin_", false),
                dateColumn("created_", true),
                versionColumn()
        );
    }

    private RowMapper<User> mapperUser() {
        return (ResultSet rs, int i) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setOpenId(rs.getString("openId_"));
            u.setEmail(rs.getString("email_"));
            u.setUsername(rs.getString("username_"));
            u.setLogo(rs.getString("logo_"));
            u.setCreated(rs.getTimestamp("created_"));
            u.setType(User.Type.valueOf(rs.getString("type_")));
            u.setLastLogin(rs.getTimestamp("lastLogin_"));
            u.setVisits(rs.getLong("visits_"));
            u.setState(User.State.valueOf(rs.getString("state_")));
            u.setExtra(rs.getString("extra_"));
            return u;
        };
    }

    private long addUser(String openId, User.Type type, String email, String username, String password, String token) {
        return insert("INSERT INTO Users(openId_, type_, email_, username_, password_, extra_, type_, state_, created_) VALUES(?,?,?,?,?,?)",
                new Object[]{openId, type.name(), email, username, encryptHelper.encrypt(password), token, User.Type.EMAIL.name(), User.State.SUBMIT.name(), new Date()},
                "id", Long.class);

    }

    private final String SELECT = "SELECT id,openId_,email_,username_,logo_,created_,type_,lastLogin_,state_ FROM Users ";

    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private StringHelper stringHelper;

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }


}
