package cn.liulingfengyu.core.config.redis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 延时删除实体
 */
@Getter
@Setter
@Builder
public class DelayedDelete implements Serializable {

    /**
     * key
     */
    private String key;

    /**
     * code
     */
    private String code;
}
