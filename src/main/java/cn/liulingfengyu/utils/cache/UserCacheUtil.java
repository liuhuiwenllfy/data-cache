package cn.liulingfengyu.utils.cache;

import cn.hutool.json.JSONUtil;
import cn.liulingfengyu.core.config.redis.entity.DelayedDelete;
import cn.liulingfengyu.core.constant.RedisConstant;
import cn.liulingfengyu.core.rabbit.config.DelayQueueConfig;
import cn.liulingfengyu.core.rabbit.producer.DelayQueueProducer;
import cn.liulingfengyu.system.entity.UserInfo;
import cn.liulingfengyu.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户缓存工具类，提供缓存操作的相关方法。
 * 包括保存、获取、删除用户信息以及处理延迟删除消息。
 *
 * @author 刘凌枫羽
 */
@Slf4j
@Component
public class UserCacheUtil implements CacheUtil<UserInfo> {

    private final static String USER = "user";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RBloomFilter<String> bloomFilter;

    @Autowired
    private DelayQueueProducer delayQueueProducer;

    /**
     * 根据用id生成Redis中的键值。
     *
     * @param id 用户id，用于标识具体的用户
     * @return 返回生成的Redis键值，格式为：RedisConstant.USER + username
     */
    public String getRedisKey(String id) {
        return RedisConstant.USER + id;
    }

    /**
     * 将用户信息保存到Redis缓存中。
     * 如果用户信息为空或用户ID为空，则不进行任何操作。
     *
     * @param userInfo 需要保存的用户信息对象
     */
    public void saveToRedis(UserInfo userInfo) {
        if (userInfo == null || userInfo.getId() == null) {
            return;
        }
        String redisKey = getRedisKey(userInfo.getId());
        bloomFilter.add(redisKey);
        String jsonStr = JSONUtil.toJsonStr(userInfo);
        redisUtil.delete(redisKey);
        redisUtil.set(redisKey, jsonStr);
        DelayedDelete delayedDelete = DelayedDelete.builder()
                .key(redisKey)
                .code(USER)
                .build();
        delayQueueProducer.sendDelayedDeleteMessage(delayedDelete);
    }

    /**
     * 从Redis缓存中获取指定用户信息。
     *
     * @param id 用户id，用于标识具体的用户
     * @return 用户信息对象，如果缓存中不存在则返回null
     */
    public UserInfo getFromRedis(String id) {
        String redisKey = getRedisKey(id);
        if (!bloomFilter.contains(redisKey)) {
            return null;
        }
        String jsonStr = redisUtil.get(redisKey);
        return jsonStr != null ? JSONUtil.toBean(jsonStr, UserInfo.class) : null;
    }

    /**
     * 从Redis中删除指定用户的缓存数据。
     *
     * @param id 用户id
     */
    public void deleteFromRedis(String id) {
        String redisKey = getRedisKey(id);
        redisUtil.delete(redisKey);
    }

    /**
     * 检查Redis中是否存在指定用户缓存数据。
     *
     * @param id 用户id
     * @return 如果存在返回true，否则返回false
     */
    public boolean hasFromRedis(String id) {
        String redisKey = getRedisKey(id);
        return redisUtil.hasKey(redisKey);
    }

    /**
     * 获取所有用户信息缓存集合。
     *
     * @return 用户信息列表
     */
    public List<UserInfo> getAllFromRedis() {
        String keyPattern = RedisConstant.USER + "*";
        Set<String> keys = redisUtil.keys(keyPattern);
        List<UserInfo> userInfoList = new ArrayList<>();
        for (String key : keys) {
            String jsonStr = redisUtil.get(key);
            if (jsonStr != null) {
                userInfoList.add(JSONUtil.toBean(jsonStr, UserInfo.class));
            }
        }
        return userInfoList;
    }

    /**
     * 处理延迟删除消息。
     * 当接收到延迟删除消息时，根据消息内容删除对应的缓存数据。
     *
     * @param delayedDelete 延迟删除消息
     * @param channel       RabbitMQ通道
     * @param tag           消息标签
     * @throws IOException IO异常
     */
    @RabbitListener(queues = DelayQueueConfig.DELAY_QUEUE)
    public void handleDelayedDelete(DelayedDelete delayedDelete, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            if (USER.equals(delayedDelete.getCode())) {
                deleteFromRedis(delayedDelete.getKey());
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Error handling delayed delete message", e);
            channel.basicNack(tag, true, false);
        }
    }
}
