package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_config")
public class ConfigBean implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1234567896L;
    @TableId(type = IdType.AUTO)
    private int configId;
    private String configContent;
    private String configMsg;
    private int configType;
    private long createTime;
    private long modifyTime;
    private int isDelete;

}
