package cn.liulingfengyu.system.service;

import cn.liulingfengyu.system.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户基表 服务类
 * </p>
 *
 * @author 刘凌枫羽
 * @since 2023-02-13
 */
public interface IUserInfoService extends IService<UserInfo> {
    /**
     * 分页查询员工表
     *
     * @param page 分页参数
     * @return {@linkplain  IPage}
     */
    IPage<UserInfo> getByPage(Page<UserInfo> page);

    /**
     * 根据用户id查询员工表
     *
     * @param id 用户id
     * @return {@link UserInfo}
     */
    UserInfo queryById(String id);

    /**
     * 新增员工表
     *
     * @param dto 员工表新增参数
     * @return boolean
     */
    boolean insertItem(UserInfo dto);

    /**
     * 修改员工表
     *
     * @param dto 员工表修改参数
     * @return boolean
     */
    boolean updateItem(UserInfo dto);

    /**
     * 批量删除员工表
     *
     * @param idList ids
     * @return boolean
     */
    boolean deleteBatchByIdList(List<String> idList);

    /**
     * 初始化用户信息到缓存。
     */
    void initUserInfoToCache();

}
