package cn.liulingfengyu.core.rabbit.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 延时队列
 *
 * @author 刘凌枫羽
 */
@Configuration
public class DelayQueueConfig {

    public static final String DELAY_QUEUE = "delay_queue";
    public static final String CACHE_DELETE_EXCHANGE = "cache_delete_exchange";
    public static final String CACHE_DELETE_ROUTING_KEY = "cache_delete_routing_key";

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE)
                .withArgument("x-message-ttl", 1000) // 延迟1秒
                .withArgument("x-dead-letter-exchange", CACHE_DELETE_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", CACHE_DELETE_ROUTING_KEY)
                .build();
    }
}
