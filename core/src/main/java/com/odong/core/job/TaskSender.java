package com.odong.core.job;

import com.odong.core.encrypt.EncryptHelper;
import com.odong.core.json.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by flamen on 13-12-30下午7:39.
 */
@Component("core.job.sender")
public class TaskSender {
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
        if(map == null){
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
    @Resource(name = "taskQueue")
    private Queue taskQueue;

    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private EncryptHelper encryptHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskSender.class);

}
