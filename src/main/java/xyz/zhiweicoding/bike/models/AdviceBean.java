package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 意见表
 *
 * @TableName t_advice
 */
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

    /**
     * @return Integer return the adviceId
     */
    public Integer getAdviceId() {
        return adviceId;
    }

    /**
     * @param adviceId the adviceId to set
     */
    public void setAdviceId(Integer adviceId) {
        this.adviceId = adviceId;
    }

    /**
     * @return String return the adviceName
     */
    public String getAdviceName() {
        return adviceName;
    }

    /**
     * @param adviceName the adviceName to set
     */
    public void setAdviceName(String adviceName) {
        this.adviceName = adviceName;
    }

    /**
     * @return long return the createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * @return long return the modifyTime
     */
    public long getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return int return the isDelete
     */
    public int getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete the isDelete to set
     */
    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

}