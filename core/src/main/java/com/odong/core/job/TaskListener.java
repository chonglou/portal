package com.odong.core.job;

import com.odong.core.model.SmtpProfile;
import com.odong.core.plugin.Plugin;
import com.odong.core.plugin.PluginUtil;
import com.odong.core.util.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
 * Created by flamen on 13-12-30下午8:10.
 */
@Component("core.job.listener")
public class TaskListener implements MessageListener, ApplicationContextAware {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @SuppressWarnings("unchecked")
    private void map(MapMessage message) {
        try {
            String type = message.getStringProperty("type");
            String module = message.getStringProperty("module");
            String itemId = message.getJMSCorrelationID();
            switch (type) {
                case "email":
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            SmtpProfile sp = cacheService.getSmtp();

                            if (sp == null) {
                                logger.error("SMTP信息未设置");
                                return;
                            }
                            try {
                                email(sp.getHost(), sp.getPort(), sp.getUsername(), sp.getPassword(),
                                        sp.isSsl(), sp.getBcc(),
                                        message.getString("to"), message.getString("title"), message.getString("body"), message.getBoolean("html"),
                                        (Map<String, String>) message.getObject("attachs"));
                            } catch (JMSException ex) {
                                logger.error("消息格式有误", ex);
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
                            logger.debug("准备给[{}]发送邮件", to);
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
                    });
                    break;
                case "gc":
                    System.gc();
                    break;
                case "rss":
                    //FIXME
                    break;
                case "qrcode":
                    //FIXME
                    break;
                case "sitemap":
                    //FIXME
                    break;
                default:
                    if (pluginUtil.isEnable(module)) {
                        logger.debug("任务转送至模块[{}]", module);
                        context.getBean("plugin."+module, Plugin.class).onMessage(message);
                    } else {
                        logger.debug("模块[{}]未启用", module);
                    }
                    break;
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

    @Resource
    private TaskExecutor taskExecutor;
    @Resource
    private CacheService cacheService;
    @Resource
    private PluginUtil pluginUtil;
    private ApplicationContext context;
    private final static Logger logger = LoggerFactory.getLogger(TaskListener.class);

    public void setPluginUtil(PluginUtil pluginUtil) {
        this.pluginUtil = pluginUtil;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}
