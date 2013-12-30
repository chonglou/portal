package com.odong.core.util;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by flamen on 13-12-30上午5:51.
 */
public class EncryptHelper {
    public String encode(String plain) {
        return te.encrypt(plain);
    }

    public String decode(String encrypt) {
        return te.decrypt(encrypt);
    }

    public String encrypt(String plain) {
        return pe.encryptPassword(plain);
    }

    public boolean check(String plain, String encrypt) {
        return pe.checkPassword(plain, encrypt);
    }

    public void init(){

        pe = new StrongPasswordEncryptor();
        //ste = new StrongTextEncryptor(); NEED JCE
        te = new BasicTextEncryptor();
        te.setPassword(key);
    }

    private StrongPasswordEncryptor pe;
    private BasicTextEncryptor te;
    private String key;

    public void setKey(String key) {
        this.key = key;
    }
}
