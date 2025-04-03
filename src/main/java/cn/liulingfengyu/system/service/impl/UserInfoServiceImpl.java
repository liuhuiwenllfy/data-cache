package cn.liulingfengyu.system.service.impl;


import cn.liulingfengyu.system.entity.UserInfo;
import cn.liulingfengyu.system.mapper.UserInfoMapper;
import cn.liulingfengyu.system.service.IUserInfoService;
import cn.liulingfengyu.utils.cache.CacheUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 用户基表 服务实现类
 * </p>
 *
 * @author 刘凌枫羽
 * @since 2023-02-13
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private CacheUtil<UserInfo> cacheUtil;

    /**
     * 分页查询
     * 注意：该分页查询只能支撑万级以下的数据量，如果数据量过大，建议采用其他方式
     *
     * @param page 分页参数
     * @return {@link IPage<UserInfo>}
     */
    @Override
    public IPage<UserInfo> getByPage(Page<UserInfo> page) {
        // 从Redis缓存中获取所有用户信息列表
        List<UserInfo> cachedList = cacheUtil.getAllFromRedis();
        // 按 username 升序
        cachedList.sort(Comparator.comparing(UserInfo::getUsername));
        // 检查缓存是否不为空
        if (!cachedList.isEmpty()) {
            // 计算当前页在缓存列表中的起始索引
            int fromIndex = Math.toIntExact((page.getCurrent() - 1) * page.getSize());
            // 处理分页溢出情况
            if (fromIndex > cachedList.size()) {
                throw new RuntimeException("分页溢出");
            }
            // 计算当前页在缓存列表中的结束索引
            int toIndex = Math.toIntExact(fromIndex + page.getSize());
            // 处理分页溢出情况
            toIndex = Math.min(toIndex, cachedList.size());

            // 从缓存列表中截取当前页的数据
            List<UserInfo> userInfoList = cachedList.subList(fromIndex, toIndex);

            // 将截取的数据设置到分页对象中
            page.setTotal(cachedList.size());
            page.setRecords(userInfoList);
            return page;
        }
        // 如果缓存为空，则直接从数据库查询分页数据
        return baseMapper.selectPage(page, null);
    }


    @Override
    public UserInfo queryById(String id) {
        // 先从缓存获取
        UserInfo userInfoVo = cacheUtil.getFromRedis(id);
        if (userInfoVo == null) {
            // 缓存未命中，从数据库查询
            userInfoVo = baseMapper.selectById(id);
            // 更新缓存
            cacheUtil.saveToRedis(userInfoVo);
        }
        return userInfoVo;
    }

    @Override
    public boolean insertItem(UserInfo dto) {
        cacheUtil.deleteFromRedis(dto.getId());
        boolean success = this.save(dto);
        if (success) {
            UserInfo userInfo = baseMapper.selectById(dto.getId());
            cacheUtil.saveToRedis(userInfo);
        }
        return success;
    }

    @Override
    public boolean updateItem(UserInfo dto) {
        cacheUtil.deleteFromRedis(dto.getId());
        boolean success = this.updateById(dto);
        if (success) {
            // 获取完整的更新后实体
            UserInfo userInfo = this.getById(dto.getId());
            cacheUtil.saveToRedis(userInfo);
        }
        return success;
    }

    @Override
    public boolean deleteBatchByIdList(List<String> idList) {
        boolean success = baseMapper.deleteBatchIds(idList) > 0;
        if (success && idList != null) {
            // 删除Redis中的缓存
            for (String id : idList) {
                cacheUtil.deleteFromRedis(id);
            }
        }
        return success;
    }

    @Async
    @Override
    public void initUserInfoToCache() {
        log.info("初始化员工信息...");
        // 从数据库获取所有员工信息
        List<UserInfo> userInfoVoList = baseMapper.selectList(null);
        userInfoVoList.forEach(userInfoVo -> cacheUtil.saveToRedis(userInfoVo));
        log.info("员工信息初始化完成，共处理{}条记录", userInfoVoList.size());
    }

}
