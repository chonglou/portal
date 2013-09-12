package com.odong.portal.job;

import com.odong.portal.model.SmtpProfile;
import com.odong.portal.service.SiteService;
import com.odong.portal.util.CacheHelper;
import com.odong.portal.util.EncryptHelper;
import com.odong.portal.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-11
 * Time: 上午7:56
 */
@Component("taskListener")
public class TaskListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            text((TextMessage) message);

        } else if (message instanceof MapMessage) {
            map((MapMessage) message);
        } else {
            logger.error("未知的消息类型");
        }
    }

    @SuppressWarnings("unchecked")
    private void map(MapMessage message) {
        try {
            String taskId = message.getStringProperty("taskId");
            String type = message.getStringProperty("type");
            String itemId = message.getJMSCorrelationID();
            if (taskId != null) {
                logger.debug("收到任务消息[{}]", taskId);
            }
            switch (type) {
                case "email":
                    taskExecutor.execute(() -> {
                        SmtpProfile sp = cacheHelper.get("site/smtp", SmtpProfile.class, null, () -> encryptHelper.decode(siteService.getString("site.smtp"), SmtpProfile.class));
                        if (sp == null) {
                            logger.error("SMTP信息未设置");
                            return;
                        }
                        try {
                            email(sp.getHost(), sp.getPort(), sp.getUsername(), sp.getPassword(), sp.isSsl(), sp.getBcc(),
                                    message.getString("to"), message.getString("title"), message.getString("body"), message.getBoolean("html"),
                                    (Map<String, String>) message.getObject("attachs"));
                        } catch (JMSException ex) {
                            logger.error("消息格式有误", ex);
                        }
                    });
                    break;
                default:
                    logger.error("未知的任务类型[{}]", type);
            }

        } catch (JMSException e) {
            logger.error("任务消息结构错误", e);

        }

    }

    private void text(TextMessage message) {
        try {
            logger.debug("收到文本消息[{}]", message.getText());
        } catch (JMSException e) {
            logger.error("处理文本消息出错", e);
        }
    }


    private void email(String host,
                       int port,
                       String username,
                       String password,
                       boolean ssl,
                       String bcc,
                       String to, String title, String body, boolean html,
                       Map<String, String> attachs) {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setProtocol(ssl ? "smtps" : "smtp");
        sender.setDefaultEncoding("UTF-8");
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.timeout", 25000);
        props.put("mail.smtp.quitwait", false);
        if (ssl) {
            props.put("mail.smtps.auth", true);
            props.put("mail.smtp.starttls.enable", ssl);
        }
        sender.setJavaMailProperties(props);

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(title);
            helper.setTo(to);
            helper.setFrom(username);
            if (bcc != null) {
                helper.setBcc(bcc);
            }
            helper.setText(body, html);
            for (String file : attachs.keySet()) {
                helper.addInline(attachs.get(file), new FileSystemResource(file));
            }
            sender.send(message);
            logger.debug("发送邮件成功[{},{}]", to, title);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }
    }


    @Resource
    private EncryptHelper encryptHelper;
    @Resource
    private SiteService siteService;
    @Resource
    private JsonHelper jsonHelper;
    @Resource
    private TaskExecutor taskExecutor;
    @Resource
    private CacheHelper cacheHelper;
    private final static Logger logger = LoggerFactory.getLogger(TaskListener.class);

    public void setCacheHelper(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setEncryptHelper(EncryptHelper encryptHelper) {
        this.encryptHelper = encryptHelper;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }
}
