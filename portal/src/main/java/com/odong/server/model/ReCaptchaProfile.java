package com.odong.server.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:59
 */
public class ReCaptchaProfile implements Serializable {
    private static final long serialVersionUID = -3640204755884600662L;
    private String privateKey;
    private String publicKey;
    private boolean includeNoScript;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isIncludeNoScript() {
        return includeNoScript;
    }

    public void setIncludeNoScript(boolean includeNoScript) {
        this.includeNoScript = includeNoScript;
    }
}
