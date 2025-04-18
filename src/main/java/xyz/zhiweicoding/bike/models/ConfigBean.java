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
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
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

    /**
     * @return int return the configId
     */
    public int getConfigId() {
        return configId;
    }

    /**
     * @param configId the configId to set
     */
    public void setConfigId(int configId) {
        this.configId = configId;
    }

    /**
     * @return String return the configContent
     */
    public String getConfigContent() {
        return configContent;
    }

    /**
     * @param configContent the configContent to set
     */
    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    /**
     * @return String return the configMsg
     */
    public String getConfigMsg() {
        return configMsg;
    }

    /**
     * @param configMsg the configMsg to set
     */
    public void setConfigMsg(String configMsg) {
        this.configMsg = configMsg;
    }

    /**
     * @return int return the configType
     */
    public int getConfigType() {
        return configType;
    }

    /**
     * @param configType the configType to set
     */
    public void setConfigType(int configType) {
        this.configType = configType;
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
