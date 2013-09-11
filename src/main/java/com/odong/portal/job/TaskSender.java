package com.odong.portal.job;

import com.odong.portal.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
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


     private void send(final String taskId, final Task.Type type, Map<String, Object> map)
{

        jmsTemplate.convertAndSend(taskQueue, map, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("taskId", taskId);
                message.setStringProperty("type", type.name());
                message.setJMSCorrelationID(taskId == null ? UUID.randomUUID().toString()
                        : taskId);
                return message;
            }
        });


        logger.debug("发送任务消息[{}]", taskId);
    }

    @PostConstruct
    void init() {
    }

    @Resource
    private JmsTemplate jmsTemplate;
    @Resource(name = "taskQueue")
    private Queue taskQueue;
    private final static Logger logger = LoggerFactory.getLogger(TaskSender.class);


    public void setTaskQueue(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

}
