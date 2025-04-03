package cn.liulingfengyu.utils.cache;

import java.util.List;


/**
 * CacheUtil接口用于提供与Redis缓存交互的通用方法。
 * 该接口定义了一系列操作Redis缓存的方法，包括获取Redis键、保存对象到Redis、从Redis获取对象、删除Redis中的对象、
 * 检查Redis中是否存在对象以及获取Redis中所有对象的功能。
 * <p>
 * 核心功能包括：
 * - 生成Redis键
 * - 保存对象到Redis缓存
 * - 从Redis缓存中获取对象
 * - 删除Redis缓存中的对象
 * - 检查Redis缓存中是否存在对象
 * - 获取Redis缓存中的所有对象
 * <p>
 * 使用示例：
 * CacheUtil<MyObject> cacheUtil = ...;
 * String key = "myKey";
 * String tenantId = "tenant1";
 * MyObject obj = new MyObject();
 * <p>
 * cacheUtil.saveToRedis(obj); // 保存对象到Redis
 * MyObject cachedObj = cacheUtil.getFromRedis(key, tenantId); // 从Redis获取对象
 * cacheUtil.deleteFromRedis(key, tenantId); // 删除Redis中的对象
 * boolean exists = cacheUtil.hasFromRedis(key, tenantId); // 检查Redis中是否存在对象
 * <p>
 * 构造函数参数：
 * 无（接口不包含构造函数）
 * <p>
 * 特殊使用限制或潜在的副作用：
 * - 使用该接口时，需要确保Redis服务已正确配置并可用。
 * - 在多租户环境下，确保传入正确的tenantId以避免数据混淆。
 */
public interface CacheUtil<T> {

    String getRedisKey(String key);

    void saveToRedis(T t);

    T getFromRedis(String key);

    void deleteFromRedis(String key);

    boolean hasFromRedis(String key);

    List<T> getAllFromRedis();
}