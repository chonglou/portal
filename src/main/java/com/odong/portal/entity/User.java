package com.odong.portal.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:29
 */
@Entity
@Table(name = "ucAccount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity {

    public User() {
    }


    public enum State {
        SUBMIT, ENABLE, DISABLE, LOCK
    }

    private static final long serialVersionUID = 6854757082522535497L;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, updatable = false)
    private Date created;
    private Date lastLogin;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;
    @Lob
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
