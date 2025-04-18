package xyz.zhiweicoding.bike.models;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_good")
public class GoodBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_UUID)
    private String goodId;
    private String goodTitle;
    private String goodBrief;
    private String scenePicUrl;
    private String listPicUrl;
    private double retailPrice;
    private int goodNumber;
    private String photoUrl;
    private String pcSymbolId;// pc端分为 目前为 轻型电摩、电动自行车、电动摩托车 三类
    @TableField(exist = false)
    private List<String> photoUrlArray;
    @TableField(exist = false)
    private String symbolName;
    private String symbolId;
    @TableField(value = "is_new")
    private int isNew;
    @TableField(value = "is_chosen")
    private int isChosen;
    private int likeNum;
    private long createTime;
    private long modifyTime;
    private int isDelete;
    @TableField(exist = false)
    private int weight;

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        if (photoUrl != null && !photoUrl.isEmpty()) {
            this.photoUrlArray = JSON.parseArray(photoUrl, String.class);
        } else {
            this.photoUrlArray = new ArrayList<>();
        }
    }

    /**
     * @return String return the goodId
     */
    public String getGoodId() {
        return goodId;
    }

    /**
     * @param goodId the goodId to set
     */
    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    /**
     * @return String return the goodTitle
     */
    public String getGoodTitle() {
        return goodTitle;
    }

    /**
     * @param goodTitle the goodTitle to set
     */
    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }

    /**
     * @return String return the goodBrief
     */
    public String getGoodBrief() {
        return goodBrief;
    }

    /**
     * @param goodBrief the goodBrief to set
     */
    public void setGoodBrief(String goodBrief) {
        this.goodBrief = goodBrief;
    }

    /**
     * @return String return the scenePicUrl
     */
    public String getScenePicUrl() {
        return scenePicUrl;
    }

    /**
     * @param scenePicUrl the scenePicUrl to set
     */
    public void setScenePicUrl(String scenePicUrl) {
        this.scenePicUrl = scenePicUrl;
    }

    /**
     * @return String return the listPicUrl
     */
    public String getListPicUrl() {
        return listPicUrl;
    }

    /**
     * @param listPicUrl the listPicUrl to set
     */
    public void setListPicUrl(String listPicUrl) {
        this.listPicUrl = listPicUrl;
    }

    /**
     * @return double return the retailPrice
     */
    public double getRetailPrice() {
        return retailPrice;
    }

    /**
     * @param retailPrice the retailPrice to set
     */
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * @return int return the goodNumber
     */
    public int getGoodNumber() {
        return goodNumber;
    }

    /**
     * @param goodNumber the goodNumber to set
     */
    public void setGoodNumber(int goodNumber) {
        this.goodNumber = goodNumber;
    }

    /**
     * @return String return the photoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * @return String return the pcSymbolId
     */
    public String getPcSymbolId() {
        return pcSymbolId;
    }

    /**
     * @param pcSymbolId the pcSymbolId to set
     */
    public void setPcSymbolId(String pcSymbolId) {
        this.pcSymbolId = pcSymbolId;
    }

    /**
     * @return List<String> return the photoUrlArray
     */
    public List<String> getPhotoUrlArray() {
        return photoUrlArray;
    }

    /**
     * @param photoUrlArray the photoUrlArray to set
     */
    public void setPhotoUrlArray(List<String> photoUrlArray) {
        this.photoUrlArray = photoUrlArray;
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
     * @return int return the isNew
     */
    public int getIsNew() {
        return isNew;
    }

    /**
     * @param isNew the isNew to set
     */
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    /**
     * @return int return the isChosen
     */
    public int getIsChosen() {
        return isChosen;
    }

    /**
     * @param isChosen the isChosen to set
     */
    public void setIsChosen(int isChosen) {
        this.isChosen = isChosen;
    }

    /**
     * @return int return the likeNum
     */
    public int getLikeNum() {
        return likeNum;
    }

    /**
     * @param likeNum the likeNum to set
     */
    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
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
     * @return int return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

}
