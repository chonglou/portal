package com.odong.server.job;

import com.odong.server.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

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

    private void map(MapMessage message) {
         try {
            String taskId = message.getStringProperty("taskId");
            Task.Type type = Task.Type.valueOf(message.getStringProperty("type"));
            String itemId = message.getJMSCorrelationID();

            if (taskId != null) {
                logger.debug("收到任务消息[{}]", taskId);
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


    private final static Logger logger = LoggerFactory.getLogger(TaskListener.class);
}
