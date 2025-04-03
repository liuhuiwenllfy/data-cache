package cn.liulingfengyu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户基表
 * </p>
 *
 * @author 刘凌枫羽
 * @since 2024-07-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_info")
public class UserInfo extends Model<UserInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;


    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
