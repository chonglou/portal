package com.odong.platform.controller.admin;

import com.odong.core.entity.Log;
import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;
import com.odong.core.service.LogService;
import com.odong.core.service.SiteService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.platform.form.admin.AllowForm;
import com.odong.platform.form.admin.GoogleAuthForm;
import com.odong.platform.form.admin.QqAuthForm;
import com.odong.web.model.ResponseItem;
import com.odong.web.model.SessionItem;
import com.odong.web.model.form.Form;
import com.odong.web.model.form.RadioField;
import com.odong.web.model.form.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-12
 * Time: 下午4:10
 */
@Controller("c.admin.state")
@RequestMapping(value = "/admin/state")
public class StateController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getStatus() {
        return "admin/state";
    }

    @RequestMapping(value = "/googleAuth", method = RequestMethod.GET)
    @ResponseBody
    Form getGoogleAuthForm() {
        Form fm = new Form("google", "google账户", "/admin/state/googleAuth");
        GoogleAuthProfile gap = siteService.get("site.googleAuth", GoogleAuthProfile.class, true);
        if (gap == null) {
            gap = new GoogleAuthProfile("", "", "");
        }
        fm.addField(new TextField<>("id", "CLIENT ID", gap.getId()));
        fm.addField(new TextField<>("secret", "CLIENT SECRET", gap.getSecret()));
        fm.addField(new TextField<>("uri", "REDIRECT URI", gap.getUri()));
        RadioField<Boolean> enable = new RadioField<Boolean>("enable", "状态", gap.isEnable());
        enable.addOption("启用", Boolean.TRUE);
        enable.addOption("停用", Boolean.FALSE);
        fm.addField(enable);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/googleAuth", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postGoogleAuthForm(@Valid GoogleAuthForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            GoogleAuthProfile gap = new GoogleAuthProfile(form.getId(), form.getSecret(), form.getUri());
            gap.setEnable(form.isEnable());
            siteService.set("site.googleAuth", gap);
            cacheService.popPage();
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点QQ互联信息", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/qqAuth", method = RequestMethod.GET)
    @ResponseBody
    Form getQqAuthForm() {
        Form fm = new Form("qq", "二维码信息", "/admin/state/qqAuth");
        QqAuthProfile qap = siteService.get("site.qqAuth", QqAuthProfile.class, true);
        if (qap == null) {
            qap = new QqAuthProfile("", "", "", "");
        }
        fm.addField(new TextField<>("valid", "验证代码", qap.getValid()));
        fm.addField(new TextField<>("id", "APP ID", qap.getId()));
        fm.addField(new TextField<>("key", "APP KEY", qap.getKey()));
        fm.addField(new TextField<>("uri", "回调路径", qap.getUri()));

        RadioField<Boolean> enable = new RadioField<Boolean>("enable", "状态", qap.isEnable());
        enable.addOption("启用", Boolean.TRUE);
        enable.addOption("停用", Boolean.FALSE);
        fm.addField(enable);
        fm.setOk(true);
        return fm;
    }

    @RequestMapping(value = "/qqAuth", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postQqAuthForm(@Valid QqAuthForm form, BindingResult result, HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            QqAuthProfile qap = new QqAuthProfile(form.getValid(), form.getId(), form.getKey(), form.getUri());
            qap.setEnable(form.isEnable());
            siteService.set("site.qqAuth", qap, true);
            cacheService.popPage();
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "修改站点QQ互联信息", Log.Type.INFO);
        }
        return ri;
    }


    @RequestMapping(value = "/runtime", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> getOsStatus() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("site.startup", siteService.get("site.startup", Date.class));

        map.put("jvm.loadedClassCount", ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
        OperatingSystemMXBean osm = ManagementFactory.getOperatingSystemMXBean();
        map.put("os.name", osm.getName());
        map.put("os.arch", osm.getArch());
        map.put("os.availableProcessors", osm.getAvailableProcessors());
        map.put("os.systemLoadAverage", osm.getSystemLoadAverage());

        RuntimeMXBean rm = ManagementFactory.getRuntimeMXBean();
        map.put("jvm.name", rm.getVmName());
        map.put("jvm.vendor", rm.getVmVendor());
        map.put("jvm.version", rm.getVmVersion());
        map.put("jvm.classPath", rm.getClassPath());
        map.put("jvm.bootClassPath", rm.getBootClassPath());
        map.put("jvm.uptime", rm.getUptime());
        map.put("jvm.startTime", rm.getStartTime());

        ThreadMXBean tm = ManagementFactory.getThreadMXBean();
        map.put("thread.threadCount", tm.getThreadCount());

        map.put("created", new Date());
        return map;
    }


    @RequestMapping(value = "/service", method = RequestMethod.GET)
    @ResponseBody
    Form getAllow() {
        Form fm = new Form("siteState", "服务状态", "/admin/state/service");
        RadioField<Boolean> login = new RadioField<>("allowLogin", "登陆", siteService.get("site.allowLogin", Boolean.class));
        login.addOption("允许", true);
        login.addOption("禁止", false);
        RadioField<Boolean> register = new RadioField<>("allowRegister", "注册", siteService.get("site.allowRegister", Boolean.class));
        register.addOption("允许", true);
        register.addOption("禁止", false);
        RadioField<Boolean> anonym = new RadioField<>("allowAnonym", "匿名评论", siteService.get("site.allowAnonym", Boolean.class));
        anonym.addOption("允许", true);
        anonym.addOption("禁止", false);
        fm.addField(login);
        fm.addField(register);
        fm.addField(anonym);
        fm.setOk(true);
        return fm;
    }


    @RequestMapping(value = "/service", method = RequestMethod.POST)
    @ResponseBody
    ResponseItem postAllow(@Valid AllowForm form, BindingResult result,HttpSession session) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.allowRegister", form.isAllowRegister());
            siteService.set("site.allowLogin", form.isAllowLogin());
            siteService.set("site.allowAnonym", form.isAllowAnonym());
            logService.add(formHelper.getSessionItem(session).getSsUserId(), "变更站点权限 注册=>[" + form.isAllowRegister() + "] 登陆=>[" + form.isAllowLogin() + "] 匿名用户=>[" + form.isAllowAnonym() + "]", Log.Type.INFO);
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
