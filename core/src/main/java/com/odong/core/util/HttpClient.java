package com.odong.core.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by flamen on 13-12-30上午5:48.
 */
public class HttpClient {

    private String encode(String key, String plain){
        BasicTextEncryptor te = new BasicTextEncryptor();
        te.setPassword(key);
        return te.encrypt(plain);
    }
}
