package com.odong.portal.util;

import com.odong.portal.service.SiteService;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
@Component
public class EncryptHelper {
    public String encode(Object plain) {
        return plain == null ? null : te.encrypt(jsonHelper.object2json(plain));
    }

    public <T> T decode(String encrypt, Class<T> clazz) {
        return encrypt == null ? null : jsonHelper.json2object(te.decrypt(encrypt), clazz);
    }

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

    @PostConstruct
    synchronized void init() {
        String key = "site.key";
        String val = siteService.getString(key);
        if (val == null) {
            val = stringHelper.random(512);
            siteService.set(key, val);
        }

        pe = new StrongPasswordEncryptor();
        //ste = new StrongTextEncryptor(); NEED JCE
        te = new BasicTextEncryptor();
        te.setPassword(val.substring(27, 39));
    }

    private StrongPasswordEncryptor pe;
    private BasicTextEncryptor te;
    @Resource
    private SiteService siteService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private StringHelper stringHelper;

    public void setStringHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
