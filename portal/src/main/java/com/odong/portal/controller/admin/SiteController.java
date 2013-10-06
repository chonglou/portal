package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.DomainForm;
import com.odong.portal.form.admin.GoogleForm;
import com.odong.portal.form.admin.InfoForm;
import com.odong.portal.form.admin.RegProtocolForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.util.CacheService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.TextAreaField;
import com.odong.portal.web.form.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午12:20
 */
@Controller("c.admin.site")
@RequestMapping(value = "/admin/site")
@SessionAttributes(SessionItem.KEY)
public class SiteController {


    @RequestMapping(value = "/regProtocol", method = RequestMethod.GET)
    @ResponseBody
    Form getRegProtocolForm() {
        Form fm = new Form("regProtocol", "用户注册协议", "/admin/site/regProtocol");
        fm.addField(new TextAreaField("protocol", "用户协议", siteService.getString("site.regProtocol")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/regProtocol", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postRegProtocolForm(@Valid RegProtocolForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.regProtocol", form.getProtocol());
            logService.add(si.getSsUserId(), "修改用户注册协议", Log.Type.INFO);
        }
        return ri;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    String getInfo() {
        return "admin/info";
    }

    @RequestMapping(value = "/google", method = RequestMethod.GET)
    @ResponseBody
    Form getGoogleForm() {
        Form fm = new Form("google", "Google Web 设置", "/admin/site/google");
        fm.addField(new TextField<>("valid", "验证文件名", siteService.getString("site.google.valid")));
        TextAreaField search = new TextAreaField("search", "自定义搜索代码", siteService.getString("site.google.search"));
        search.setHtml(false);
        fm.addField(search);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/google", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postGoogleForm(@Valid GoogleForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.google.valid", form.getValid().trim());
            siteService.set("site.google.search", form.getSearch());
            logService.add(si.getSsUserId(), "修改google配置", Log.Type.INFO);
            cacheService.popGoogleSearch();
            cacheService.popGoogleValidCode();
        }
        return ri;
    }

    @RequestMapping(value = "/domain", method = RequestMethod.GET)
    @ResponseBody
    Form getDomainForm() {
        Form fm = new Form("domain", "域名设置", "/admin/site/domain");
        fm.addField(new TextField<>("domain", "域名", siteService.getString("site.domain")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/domain", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDomainForm(@Valid DomainForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            String domain = form.getDomain().trim();
            siteService.set("site.domain", domain);
            logService.add(si.getSsUserId(), "修改站点域名", Log.Type.INFO);
            cacheService.popSiteInfo();
            cacheService.popDomain();


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


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    Form getDetailsForm() {
        Form fm = new Form("details", "站点信息编辑", "/admin/site/details");
        fm.addField(new TextField<>("title", "名称", siteService.getString("site.title")));
        fm.addField(new TextField<>("keywords", "关键字", siteService.getString("site.keywords"), "用空格隔开"));
        TextAreaField desc = new TextAreaField("description", "说明", siteService.getString("site.description"));
        desc.setHtml(false);
        fm.addField(desc);
        fm.addField(new TextField<>("copyright", "版权", siteService.getString("site.copyright")));
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postDetailsForm(@Valid InfoForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.title", form.getTitle());
            siteService.set("site.keywords", form.getKeywords());
            siteService.set("site.description", form.getDescription());
            siteService.set("site.copyright", form.getCopyright());
            logService.add(si.getSsUserId(), "修改站点基本信息", Log.Type.INFO);
            cacheService.popSiteInfo();
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
    @Value("${app.store}")
    private String appStoreDir;
    private final static Logger logger = LoggerFactory.getLogger(SiteController.class);

    public void setAppStoreDir(String appStoreDir) {
        this.appStoreDir = appStoreDir;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }


    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }
}
