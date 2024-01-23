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
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_symbol")
public class SymbolBean implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_UUID)
    private String symbolId;
    private String symbolName;
    private int sortNum;
    private int isPopular;
    private int place;
    private long createTime;
    private long modifyTime;
    private int isDelete;
    @TableField(exist = false)
    private List<GoodBean> goodBeans;
    @TableField(exist = false)
    private boolean checked;
}
