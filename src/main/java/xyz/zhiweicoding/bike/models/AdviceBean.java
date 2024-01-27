package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 意见表
 *
 * @TableName t_advice
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_advice")
public class AdviceBean implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer adviceId;
    private String adviceName;
    private long createTime;
    private long modifyTime;
    private int isDelete;
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = -3859701151987435175L;
}