package com.odong.core.encrypt;

/**
 * Created by flamen on 13-12-30上午5:51.
 */
public interface EncryptHelper {
    String encode(String plain);

    String decode(String encrypt);

    String encrypt(String plain);

    boolean check(String plain, String encrypt);

    void init();
}
