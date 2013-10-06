package com.odong.portal.controller.admin;

import com.odong.portal.entity.Log;
import com.odong.portal.form.admin.AllowForm;
import com.odong.portal.model.SessionItem;
import com.odong.portal.util.CacheService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.FormHelper;
import com.odong.portal.web.ResponseItem;
import com.odong.portal.web.form.Form;
import com.odong.portal.web.form.RadioField;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@SessionAttributes(SessionItem.KEY)
public class StateController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getStatus() {
        return "admin/state";
    }

    @RequestMapping(value = "/runtime", method = RequestMethod.POST)
    @ResponseBody
    Map<String, Object> getOsStatus() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("site.startup", cacheService.getStartUp());
        map.put("memcached", cacheHelper.status());
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
        RadioField<Boolean> login = new RadioField<>("allowLogin", "登陆", siteService.getBoolean("site.allowLogin"));
        login.addOption("允许", true);
        login.addOption("禁止", false);
        RadioField<Boolean> register = new RadioField<>("allowRegister", "注册", siteService.getBoolean("site.allowRegister"));
        register.addOption("允许", true);
        register.addOption("禁止", false);
        RadioField<Boolean> anonym = new RadioField<>("allowAnonym", "匿名评论", siteService.getBoolean("site.allowAnonym"));
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
    ResponseItem postAllow(@Valid AllowForm form, BindingResult result, @ModelAttribute(SessionItem.KEY) SessionItem si) {
        ResponseItem ri = formHelper.check(result);
        if (ri.isOk()) {
            siteService.set("site.allowRegister", form.isAllowRegister());
            siteService.set("site.allowLogin", form.isAllowLogin());
            siteService.set("site.allowAnonym", form.isAllowAnonym());
            logService.add(si.getSsUserId(), "变更站点权限 注册=>[" + form.isAllowRegister() + "] 登陆=>[" + form.isAllowLogin() + "] 匿名用户=>[" + form.isAllowAnonym() + "]", Log.Type.INFO);
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
    private CacheHelper cacheHelper;
    @Resource
    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
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
