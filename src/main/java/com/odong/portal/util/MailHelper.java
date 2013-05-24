package com.odong.portal.util;

import com.odong.portal.model.SmtpProfile;
import com.odong.portal.service.SiteService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午5:08
 */
@Component
public class MailHelper {
    public void send(String to, String title, String body, boolean html) {

    }

    public boolean isAvailable() {
        return profile != null;
    }

    public void setup(SmtpProfile profile) {
        siteService.set("site.smtp", encryptHelper.encode(profile));
        this.profile = profile;
    }

    @PostConstruct
    public void reload() {
        profile = encryptHelper.decode(siteService.getString("site.map"), SmtpProfile.class);
    }

    @Resource
    private SiteService siteService;
    @Resource
    private EncryptHelper encryptHelper;
    private SmtpProfile profile;


    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }
}
