package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.job.TaskSender;
import com.odong.core.model.QrCodeProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.*;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.TextAreaField;
import com.odong.web.model.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 14-1-5下午10:16.
 */
@Controller("platform.c.admin.info")
@RequestMapping(value = "/admin/site")
public class SiteController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getInfo() {
        return "/platform/admin/site";
    }

    @RequestMapping(value = "/domain", method = RequestMethod.GET)
    @ResponseBody
    Form getDomainForm() {
        Form fm = new Form("domain", "域名设置", "/admin/site/domain");
        fm.addField(new TextField<>("domain", "域名", siteService.get("site.domain", String.class)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/domain", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDomainForm(@Valid DomainForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            String domain = form.getDomain().trim();
            siteService.set("site.domain", domain);
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点域名", Log.Type.INFO);

            cacheService.popSiteDomain();
            Path file = Paths.get(appStoreDir + "/seo/robots.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"))) {
                writer.write("User-agent: *\n");
                writer.write("Disallow: /admin/\n");
                writer.write("Disallow: /personal/\n");
                writer.write("Sitemap: http://" + domain + "/sitemap.xml.gz\n");
            } catch (IOException e) {
                logger.error("生成robots.txt文件出错", e);
            }
        }
        return ri;
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.GET)
    @ResponseBody
    Form getAboutMe() {
        Form fm = new Form("aboutMe", "设置关于我们", "/admin/site/aboutMe/");
        fm.addField(new TextAreaField("content", "内容", siteService.get("site.aboutMe", String.class)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/aboutMe", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAboutMe(@Valid AboutMeForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.aboutMe", form.getContent());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "设置关于我们", Log.Type.INFO);
            cacheService.popAboutMe();
        }
        return ri;

    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    Form getDetailsForm() {
        Form fm = new Form("details", "站点信息编辑", "/admin/site/details");
        fm.addField(new TextField<>("title", "名称", siteService.get("site.title", String.class)));
        fm.addField(new TextField<>("keywords", "关键字", siteService.get("site.keywords", String.class), "用空格隔开"));
        TextAreaField desc = new TextAreaField("description", "说明", siteService.get("site.description", String.class));
        desc.setHtml(false);
        fm.addField(desc);
        fm.addField(new TextField<>("copyright", "版权", siteService.get("site.copyright", String.class)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDetailsForm(@Valid InfoForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点基本信息", Log.Type.INFO);
            cacheService.popSiteTitle();
            cacheService.popPage();
        }
        return ri;
    }


    @RequestMapping(value = "/qrCode", method = RequestMethod.GET)
    @ResponseBody
    Form getQrForm() {
        Form fm = new Form("qrCode", "二维码信息", "/admin/site/qrCode");
        QrCodeProfile qcp = siteService.get("site.qrCode", QrCodeProfile.class);
        if (qcp == null) {
            qcp = new QrCodeProfile("", 300, 300);
        }
        fm.addField(new TextField<Integer>("width", "宽度", qcp.getWidth()));
        fm.addField(new TextField<Integer>("height", "高度", qcp.getHeight()));
        TextAreaField taf = new TextAreaField("content", "内容", qcp.getContent());
        taf.setHtml(false);
        fm.addField(taf);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/qrCode", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postQrForm(@Valid QrCodeForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.qrCode", new QrCodeProfile(form.getContent(), form.getWidth(), form.getHeight()));
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点二维码信息", Log.Type.INFO);
            Map<String, Object> map = new HashMap<>();
            map.put("width", form.getHeight());
            map.put("content", form.getContent());
            map.put("height", form.getHeight());
            taskSender.send(null, "qrcode", map);
        }
        return ri;
    }


    @RequestMapping(value = "/regProtocol", method = RequestMethod.GET)
    @ResponseBody
    Form getRegProtocolForm() {
        Form fm = new Form("regProtocol", "用户注册协议", "/admin/site/regProtocol");
        fm.addField(new TextAreaField("protocol", "用户协议", siteService.get("site.regProtocol", String.class)));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/regProtocol", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegProtocolForm(@Valid RegProtocolForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.regProtocol", form.getProtocol());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改用户注册协议", Log.Type.INFO);
        }
        return ri;
    }


    @Resource
    private LogService logService;
    @Resource
    private SiteService siteService;
    @Resource
    private FormHelper formHelper;
    @Resource
    private CacheService cacheService;
    @Resource
    private TaskSender taskSender;
    @Value("${app.store}")
    private String appStoreDir;
    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }
}
