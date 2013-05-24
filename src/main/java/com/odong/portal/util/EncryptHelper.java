package com.odong.portal.util;

import com.odong.portal.service.SiteService;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn("siteHelper")
public class EncryptHelper {
    public String encode(Object plain) {
        return plain == null ? null : ste.encrypt(jsonHelper.object2json(plain));
    }

    public <T> T decode(String encrypt, Class<T> clazz) {
        return encrypt == null ? null : jsonHelper.json2object(ste.decrypt(encrypt), clazz);
    }

    public String encode(String plain) {
        return ste.encrypt(plain);
    }

    public String decode(String encrypt) {
        return ste.decrypt(encrypt);
    }

    public String encrypt(String plain) {
        return spe.encryptPassword(plain);
    }

    public boolean check(String plain, String encrypt) {
        return spe.checkPassword(plain, encrypt);
    }

    @PostConstruct
    void init() {
        spe = new StrongPasswordEncryptor();
        ste = new StrongTextEncryptor();
        ste.setPassword(siteService.getString("site.key").substring(27, 39));
    }

    private StrongPasswordEncryptor spe;
    private StrongTextEncryptor ste;
    @Resource
    private SiteService siteService;
    @Resource
    private JsonHelper jsonHelper;

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
