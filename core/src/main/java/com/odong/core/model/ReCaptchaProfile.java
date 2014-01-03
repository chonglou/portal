package com.odong.core.model;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-3上午10:02.
 */
public class ReCaptchaProfile implements Serializable {
    private static final long serialVersionUID = -1170051234521804541L;
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

