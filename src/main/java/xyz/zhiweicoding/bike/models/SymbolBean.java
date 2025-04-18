package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
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

    /**
     * @return String return the symbolId
     */
    public String getSymbolId() {
        return symbolId;
    }

    /**
     * @param symbolId the symbolId to set
     */
    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    /**
     * @return String return the symbolName
     */
    public String getSymbolName() {
        return symbolName;
    }

    /**
     * @param symbolName the symbolName to set
     */
    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    /**
     * @return int return the sortNum
     */
    public int getSortNum() {
        return sortNum;
    }

    /**
     * @param sortNum the sortNum to set
     */
    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    /**
     * @return int return the isPopular
     */
    public int getIsPopular() {
        return isPopular;
    }

    /**
     * @param isPopular the isPopular to set
     */
    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    /**
     * @return int return the place
     */
    public int getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(int place) {
        this.place = place;
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

    /**
     * @return List<GoodBean> return the goodBeans
     */
    public List<GoodBean> getGoodBeans() {
        return goodBeans;
    }

    /**
     * @param goodBeans the goodBeans to set
     */
    public void setGoodBeans(List<GoodBean> goodBeans) {
        this.goodBeans = goodBeans;
    }

    /**
     * @return boolean return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
