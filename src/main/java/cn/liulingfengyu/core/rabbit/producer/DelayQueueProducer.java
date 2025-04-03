package cn.liulingfengyu.core.rabbit.producer;

import cn.liulingfengyu.core.config.redis.entity.DelayedDelete;
import cn.liulingfengyu.core.rabbit.config.DelayQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 延时消息生产者
 *
 * @author 刘凌枫羽
 */
@Service
public class DelayQueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelayedDeleteMessage(DelayedDelete delayedDelete) {
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_QUEUE, delayedDelete);
    }
}
