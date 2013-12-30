package com.odong.daemon.util;

import org.jasypt.util.text.BasicTextEncryptor;
/**
 * Created by flamen on 13-12-30上午5:29.
 */
public class EncryptHelper {
    public EncryptHelper(String key) {
        this.te = new BasicTextEncryptor();
        te.setPassword(key);
    }
    public String decode(String encrypt){
        return te.decrypt(encrypt);
    }
    private final BasicTextEncryptor te;

}
