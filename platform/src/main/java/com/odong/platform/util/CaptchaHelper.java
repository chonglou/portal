package com.odong.platform.util;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.odong.core.model.KaptchaProfile;
import com.odong.core.model.ReCaptchaProfile;
import com.odong.core.service.SiteService;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by flamen on 14-1-3上午9:52.
 */
@Component("platform.captchaHelper")
public class CaptchaHelper {

    @PostConstruct
    public void reload() {
        KaptchaProfile kp = siteService.get("site.kaptcha", KaptchaProfile.class);

        if (kp == null) {
            kp = new KaptchaProfile();
            kp.setLength(4);
            kp.setChars("0123456789");
            kp.setHeight(56);
            kp.setWidth(100);
            siteService.set("site.kaptcha", kp);
            siteService.set("site.captcha", "kaptcha");
        }
        Properties props = new Properties();
        props.setProperty("kaptcha.image.width", Integer.toString(kp.getWidth()));
        props.setProperty("kaptcha.image.height", Integer.toString(kp.getHeight()));
        props.setProperty("kaptcha.textproducer.char.string", kp.getChars());
        props.setProperty("kaptcha.textproducer.char.length", Integer.toString(kp.getLength()));
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(new Config(props));
        this.kaptcha = kaptcha;


        ReCaptchaProfile rp = siteService.get("site.reCaptcha", ReCaptchaProfile.class, true);
        if (rp == null) {
            rp = new ReCaptchaProfile("", "");
            siteService.set("site.reCaptcha", rp, true);
        }
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(rp.getPrivateKey());
        reCaptcha.setPublicKey(rp.getPublicKey());
        reCaptcha.setIncludeNoscript(rp.isIncludeNoScript());
        this.reCaptcha = reCaptcha;
        logger.info("重新加载验证码配置");
    }


    private Producer kaptcha;
    private ReCaptcha reCaptcha;
    @Resource
    private SiteService siteService;
    private final static Logger logger = LoggerFactory.getLogger(CaptchaHelper.class);


    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public Producer getKaptcha() {
        return kaptcha;
    }

    public ReCaptcha getReCaptcha() {
        return reCaptcha;
    }

}
