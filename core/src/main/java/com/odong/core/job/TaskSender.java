package com.odong.core.job;

import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import com.odong.core.util.CacheService;
import com.odong.core.util.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by flamen on 13-12-30下午7:39.
 */
@Component("core.job.sender")
public class TaskSender {
    public void valid(String to, String type, String action, String content, Map<String, Object> args) {
        int valid = cacheService.getLinkValid();
        args.put("type", type);
        args.put("email", to);
        args.put("valid", timeHelper.plus(new Date(), valid * 60).getTime());

        String domain = cacheService.getSiteDomain();
        String title = cacheService.getSiteTitle();

        try {

            email(to, "您在[" + domain + "(" + title + ")]上"
                    + action + "，请激活",
                    "<a href='http://" + domain + "/personal/valid?code=" +
                            URLEncoder.encode(encryptHelper.encode(jsonHelper.object2json(
                                    args)), "UTF-8")
                            + "' target='_blank'>请点击此链接以" + content + "(" + valid + "分钟内有效)</a>。" +
                            "<br/>如果不是您的操作，请忽略该邮件。",
                    true, null);
        } catch (UnsupportedEncodingException e) {
            logger.error("生成邮件正文出错", e);
        }
    }

    public void email(String to, String title, String body, boolean html, Map<String, String> attachs) {
        Map<String, Object> map = new HashMap<>();
        map.put("to", to);
        map.put("title", title);
        map.put("body", body);
        map.put("html", html);
        if (attachs == null) {
            attachs = new HashMap<>();
        }
        map.put("attachs", attachs);
        send(null, "email", map);
    }

    public void send(String module, String type, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        jmsTemplate.convertAndSend(taskQueue, map, (message) -> {
            message.setStringProperty("type", type);
            message.setStringProperty("module", module);
            message.setJMSCorrelationID(UUID.randomUUID().toString());
            return message;
        }
        );

        //logger.debug("发送任务消息[{}]", taskId);
    }

    @Resource
    private JmsTemplate jmsTemplate;
    @Resource(name = "core.taskQueue")
    private Queue taskQueue;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private CacheService cacheService;
    @Resource
    private TimeHelper timeHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskSender.class);

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setTaskQueue(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
