package com.odong.portal.job;

import com.odong.portal.entity.Task;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.Queue;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午7:52
 */
@Component
public class TaskSender {
    public void visitArticle(long article) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "article");
        map.put("id", article);
        send(Task.Type.VISIT, map);
    }

    public void visitTag(long tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "tag");
        map.put("id", tag);
        send(Task.Type.VISIT, map);
    }

    public void export2json() {
        send(Task.Type.EXPORT, new HashMap<String, Object>());
    }

    public void import4json(String filename) {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", filename);
        send(Task.Type.IMPORT, map);
    }

    public void gc() {
        send(Task.Type.GC, new HashMap<String, Object>());
    }

    public void rss() {
        send(Task.Type.RSS, new HashMap<String, Object>());
    }

    public void backup() {
        send(Task.Type.BACKUP, new HashMap<String, Object>());
    }

    public void sitemap() {
        send(Task.Type.SITE_MAP, new HashMap<String, Object>());
    }

    public void validEmail(String to, String type, Map<String, Object> args) {
        args.put("valid", linkValid);
        args.put("type", type);
        args.put("email", to);
        args.put("created", jsonHelper.object2json(new Date()));

        String action = "";
        String content = "";
        switch (type) {
            case "register":
                action = "创建了账户";
                content = "激活账户";
                break;
            case "reset_pwd":
                action = "重置了密码";
                content = "重置密码";
                break;
        }


        String domain = cacheHelper.get("site/domain", String.class, null, () -> siteService.getString("site.domain"));
        String title = cacheHelper.get("site/title", String.class, null, () -> siteService.getString("site.title"));

        try {

            email(to, "您在[" + domain + "(" + title + ")]上"
                    + action + "，请激活",
                    "<a href='http://" + domain + "/personal/valid?code=" +
                            URLEncoder.encode(encryptHelper.encode(jsonHelper.object2json(
                                    args)), "UTF-8")

                            + "' target='_blank'>请点击此链接以" + content + "(" + linkValid + "分钟内有效)</a>。" +
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
        send(Task.Type.EMAIL, map);
    }

    private void send(final Task.Type type, Map<String, Object> map) {

        jmsTemplate.convertAndSend(taskQueue, map, (message) -> {
            message.setStringProperty("type", type.name());
            message.setJMSCorrelationID(UUID.randomUUID().toString());
            return message;
        }
        );

        //logger.debug("发送任务消息[{}]", taskId);
    }

    @PostConstruct
    void init() {
    }

    @Resource
    private JmsTemplate jmsTemplate;
    @Resource(name = "taskQueue")
    private Queue taskQueue;
    @Value("${link.valid}")
    protected int linkValid;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private CacheHelper cacheHelper;
    @Resource
    private SiteService siteService;
    private final static Logger logger = LoggerFactory.getLogger(TaskSender.class);

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setTaskQueue(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    public void setLinkValid(int linkValid) {
        this.linkValid = linkValid;
    }

}
