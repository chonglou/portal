package com.odong.portal.controller.personal;


import com.odong.portal.entity.Log;
import com.odong.portal.entity.User;
import com.odong.portal.job.TaskSender;
import com.odong.portal.model.SessionItem;
import com.odong.portal.service.AccountService;
import com.odong.portal.service.LogService;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.TimeHelper;
import com.odong.portal.web.ResponseItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:21
 */
@Controller("c.personal.valid")
@RequestMapping(value = "/personal/valid")
@SessionAttributes(SessionItem.KEY)
public class ValidController {

    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    String getValidCode(HttpServletRequest request, Map<String, Object> map) {

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        Map<String, String> mapA = jsonHelper.json2map(encryptHelper.decode(request.getParameter("code")), String.class, String.class);
        String email = mapA.get("email");
        String type = mapA.get("type");
        String created = mapA.get("created");

        if (email == null || type == null || created == null) {
            ri.addData("链接信息不全");
        } else if (timeHelper.plus(new Date(Long.parseLong(created)), 60 * linkValid).compareTo(new Date()) < 0
                ) {
            ri.addData("链接失效，请重新申请。");
        } else {
            User u = accountService.getUser(email);

            switch (type) {
                case "register":
                    if (u != null && u.getState() == User.State.SUBMIT) {
                        accountService.setUserState(u.getId(), User.State.ENABLE);
                        logService.add(u.getId(), "账户激活", Log.Type.INFO);
                        taskSender.sendEmail(email, "您在[" + siteService.getString("site.title") + "]上的激活了账户", "欢迎使用", true, null);
                        ri.setOk(true);

                    } else {
                        ri.addData("账户[" + email + "]状态不对");
                    }
                    break;
                case "reset_pwd":
                    if (u.getState() == User.State.DISABLE) {
                        if (new Date().compareTo(timeHelper.plus(jsonHelper.json2object(mapA.get("created"), Date.class), 60 * 30)) <= 0) {
                            accountService.setUserPassword(u.getId(), mapA.get("password"));
                            logService.add(u.getId(), "重置密码", Log.Type.INFO);
                            taskSender.sendEmail(email, "您在[" + siteService.getString("site.domain") + "]上的成功重置了密码",
                                    "如果不是您的操作，请忽略该邮件。", true, null);
                            ri.setOk(true);
                            ri.addData("您成功重置了密码");
                        } else {
                            ri.addData("链接已失效");
                        }
                    } else {
                        ri.addData("用户[" + u.getEmail() + "]状态不对");
                    }
                    break;
                default:
                    ri.addData("未知的操作");
                    break;
            }
        }
        map.put("item", ri);
        return "message";
    }


    @Resource
    private AccountService accountService;
    @Resource
    private TimeHelper timeHelper;
    @Resource
    private LogService logService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TaskSender taskSender;
    @Resource
    private SiteService siteService;
    @Resource
    private EncryptHelper encryptHelper;
    @Value("${link.valid}")
    protected int linkValid;

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setLinkValid(int linkValid) {
        this.linkValid = linkValid;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
