package com.odong.platform.controller.personal;


import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.entity.Log;
import com.odong.core.entity.User;
import com.odong.core.job.TaskSender;
import com.odong.core.json.JsonHelper;
import com.odong.core.service.LogService;
import com.odong.core.service.UserService;
import com.odong.core.util.CacheService;
import com.odong.core.util.FormHelper;
import com.odong.core.util.TimeHelper;
import com.odong.web.model.Page;
import com.odong.web.model.ResponseItem;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-13
 * Time: 下午2:21
 */
@Controller("platform.c.personal.valid")
@RequestMapping(value = "/personal")
public class ValidController {

    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    String getValidCode(HttpServletRequest request, Map<String, Object> map) {

        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        Map<String, String> mapA;
        try {
            mapA = jsonHelper.json2map(encryptHelper.decode(request.getParameter("code")), String.class, String.class);
        } catch (EncryptionOperationNotPossibleException e) {
            logger.error("解析code出错", e);
            mapA = new HashMap<>();

        }

        String email = mapA.get("email");
        String type = mapA.get("type");
        String valid = mapA.get("valid");

        if (email == null || type == null || valid == null) {
            ri.addData("链接信息不全");
        } else if (new Date(Long.parseLong(valid)).compareTo(new Date()) < 0) {
            ri.addData("链接失效，请重新申请。");
        } else {
            User u = userService.getUser(email, User.Type.EMAIL);

            switch (type) {
                case "register":
                    if (u != null && u.getState() == User.State.SUBMIT) {
                        userService.setUserState(u.getId(), User.State.ENABLE);
                        logService.add(u.getId(), "账户激活", Log.Type.INFO);
                        taskSender.email(email, "您在[" + cacheService.getSiteTitle() + "]上的激活了账户", "欢迎使用", true, null);
                        ri.setOk(true);

                    } else {
                        ri.addData("账户[" + email + "]状态不对");
                    }
                    break;
                case "reset_pwd":
                    if (u.getState() == User.State.ENABLE) {
                        if (new Date().compareTo(timeHelper.plus(jsonHelper.json2object(mapA.get("created"), Date.class), 60 * 30)) <= 0) {
                            userService.setUserPassword(u.getId(), mapA.get("password"));
                            logService.add(u.getId(), "重置密码", Log.Type.INFO);
                            taskSender.email(email, "您在[" + cacheService.getSiteTitle() + "]上的成功重置了密码",
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

        Page page = formHelper.getPage(request.getSession());
        page.setIndex("/personal/self");
        map.put("page", page);
        map.put("message", ri);
        return "/core/message";
    }


    @Resource
    private FormHelper formHelper;
    @Resource
    private UserService userService;
    @Resource
    private TimeHelper timeHelper;
    @Resource
    private LogService logService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TaskSender taskSender;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private final static Logger logger = LoggerFactory.getLogger(ValidController.class);

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFormHelper(FormHelper formHelper) {
        this.formHelper = formHelper;
    }

    public void setTaskSender(TaskSender taskSender) {
        this.taskSender = taskSender;
    }


    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }


    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

}
