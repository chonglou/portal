package com.odong.core.service.impl;

import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.entity.User;
import com.odong.core.json.JsonHelper;
import com.odong.core.model.Contact;
import com.odong.core.service.UserService;
import com.odong.core.store.JdbcHelper;
import com.odong.core.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by flamen on 13-12-31下午3:22.
 */
@Component("core.userService")
public class UserServiceImpl extends JdbcHelper implements UserService {

    @Override
    public void addGoogleUser(String openId, String token) {
        String username = stringHelper.random(8);
        addUser(openId, username + "@localhost", username, stringHelper.random(12), null);
    }

    @Override
    public void addQqUser(String openId, String accessToken, String username) {
        addUser(openId, stringHelper.random(8) + "@localhost", username, stringHelper.random(12), accessToken);
    }

    @Override
    public List<User> listUser() {
        return list(SELECT, mapperUser());
    }

    @Override
    public void setUserLogin(long user) {
        execute("UPDATE users SET lastLogin_=?,version=version+1 WHERE id=?", new Date(), user);
    }

    @Override
    public void setUserEmail(long user, String email) {
        execute("UPDATE users SET email_=?,version=version+1 WHERE id=?", email, user);
    }

    @Override
    public User getUser(long id) {
        return select(SELECT + " WHERE id=?", new Object[]{id}, mapperUser());
    }

    @Override
    public void addUser(String email, String username, String password) {
        addUser(UUID.randomUUID().toString(), email, username, password, null);
    }


    @Override
    public void setUserContact(long user, Contact contact) {
        execute("UPDATE users SET contact_=?,version=version+1 WHERE id=?", jsonHelper.object2json(contact), user);
    }

    @Override
    public void setUserName(long user, String username) {
        execute("UPDATE users SET username_=?,version=version+1 WHERE id=?", username, user);
    }

    @Override
    public void setUserPassword(long user, String password) {
        execute("UPDATE users SET password_=?,version=version+1 WHERE id=?", encryptHelper.encrypt(password), user);
    }

    @Override
    public void setUserState(long user, User.State state) {
        execute("UPDATE users SET state_=?,version=version+1 WHERE id=?", state.name(), user);
    }

    @Override
    public boolean auth(String email, String password) {
        String pwd = select("SELECT password_ FROM users WHERE email_=?", new Object[]{email}, String.class);
        return pwd != null && encryptHelper.check(password, pwd);
    }

    @PostConstruct
    void init() {
        install("users",
                longIdColumn("id"),
                stringColumn("openId_", 255, true, true),
                stringColumn("email_", 255, true, true),
                stringColumn("username_", 255, true, false),
                stringColumn("password_", 512, true, false),
                stringColumn("logo_", 255, false, false),
                textColumn("extra_", false),
                textColumn("contact_", true),
                enumColumn("type_"),
                enumColumn("state_"),
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
            u.setState(User.State.valueOf(rs.getString("state_")));
            return u;
        };
    }

    private void addUser(String openId, String email, String username, String password, String token) {
        execute("INSERT INTO users(openId_, email_, username_, password_, extra_, type_, state_, created_) VALUES(?,?,?,?,?,?)",
                openId, email, username, encryptHelper.encrypt(password), token, User.Type.EMAIL.name(), User.State.SUBMIT.name(), new Date());

    }

    private final String SELECT = "SELECT id,openId_,email_,username_,logo_,created_,type_,lastLogin_,state_ FROM users";

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

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Value("${jdbc.driver}")
    public void setDriver(String driver) {
        jdbcDriver = driver;
    }

}
