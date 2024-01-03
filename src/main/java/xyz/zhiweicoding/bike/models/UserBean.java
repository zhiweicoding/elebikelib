package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表
 *
 * @TableName t_user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_user")
public class UserBean implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1234567896L;
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private Integer userId;
    private String openId;
    private String nickName;
    private String userMobile;
    private String avatarUrl;
    private String unionId;
    private String province;
    private String city;
    private String country;
    private String gender;
    private String language;
    private long createTime;
    private long modifyTime;
    private int isDelete;
}