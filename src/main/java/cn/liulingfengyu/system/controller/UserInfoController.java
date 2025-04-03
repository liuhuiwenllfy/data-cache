package cn.liulingfengyu.system.controller;

import cn.liulingfengyu.system.entity.UserInfo;
import cn.liulingfengyu.system.service.IUserInfoService;
import cn.liulingfengyu.utils.RespJson;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户基表 前端控制器
 * </p>
 *
 * @author 刘凌枫羽
 * @since 2023-02-13
 */
@RestController
@RequestMapping("/system/userInfo")
@AllArgsConstructor
public class UserInfoController {

    private final IUserInfoService service;

    /**
     * 条件分页查询员工表
     *
     * @param page 分页参数
     * @return {@link RespJson}
     */
    @GetMapping("getByPage")
    public RespJson<IPage<UserInfo>> getByPage(Page<UserInfo> page) {
        return RespJson.success(service.getByPage(page));
    }

    /**
     * 根据用户id查询员工表
     *
     * @param id 用户id
     * @return {@link RespJson}
     */
    @GetMapping("queryById")
    public RespJson<UserInfo> queryById(String id) {
        return RespJson.success(service.queryById(id));
    }

    /**
     * 新增员工表
     *
     * @param dto 新增参数
     * @return {@link RespJson}
     */
    @PostMapping("insert")
    public RespJson<Boolean> insert(@RequestBody @Validated UserInfo dto) {
        return RespJson.insertState(service.insertItem(dto));
    }

    /**
     * 修改员工表
     *
     * @param dto 修改参数
     * @return {@link RespJson}
     */
    @PutMapping("updateById")
    public RespJson<Boolean> updateById(@RequestBody @Validated UserInfo dto) {
        return RespJson.updateState(service.updateItem(dto));
    }

    /**
     * 批量删除员工表
     *
     * @param idList ids
     * @return {@link RespJson}
     */
    @DeleteMapping("deleteBatchByIdList")
    public RespJson<Boolean> deleteBatchByIdList(@RequestParam List<String> idList) {
        return RespJson.deleteState(service.deleteBatchByIdList(idList));
    }

    /**
     * 初始化用户信息到缓存。
     * 该方法会从数据库中获取所有用户信息，并将其存储到Redis缓存中。
     * 适用于系统启动时或缓存数据丢失时重新加载用户信息。
     *
     * @return {@link RespJson} 返回操作结果，true表示成功，false表示失败。
     */
    @PostMapping("initUserInfoToCache")
    public RespJson<Boolean> initUserInfoToCache() {
        // 调用服务层方法，初始化用户信息到缓存
        service.initUserInfoToCache();
        return RespJson.state(true);
    }

}
