package com.odong.core.encrypt.impl;

import com.odong.core.encrypt.EncryptHelper;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by flamen on 13-12-30下午7:45.
 */

public class EncryptJasyptHelperImpl implements EncryptHelper {
    @Override
    public String encode(String plain) {
        return te.encrypt(plain);
    }

    @Override
    public String decode(String encrypt) {
        return te.decrypt(encrypt);
    }

    @Override
    public String encrypt(String plain) {
        return pe.encryptPassword(plain);
    }

    @Override
    public boolean check(String plain, String encrypt) {
        return pe.checkPassword(plain, encrypt);
    }

    @Override
    public void init() {

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
