package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 门店
 *
 * @TableName t_store
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_store")
public class StoreBean implements Serializable {
    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_UUID)
    private String storeId;
    private String storeName;
    private String storeDesc;
    private String storeLogo;
    private String phoneNum;
    private String backupPhoneNum;
    private String staffWx;
    private String address;
    private String lnglat;
    private String licenseUrl;
    private int isDelete;
    private long createTime;
    private long modifyTime;
    @TableField(exist = false)
    private double distance;
    @TableField(exist = false)
    private double lng;
    @TableField(exist = false)
    private double lat;

    /**
     * @return String return the storeId
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * @param storeId the storeId to set
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * @return String return the storeName
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * @param storeName the storeName to set
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return String return the storeDesc
     */
    public String getStoreDesc() {
        return storeDesc;
    }

    /**
     * @param storeDesc the storeDesc to set
     */
    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    /**
     * @return String return the storeLogo
     */
    public String getStoreLogo() {
        return storeLogo;
    }

    /**
     * @param storeLogo the storeLogo to set
     */
    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    /**
     * @return String return the phoneNum
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phoneNum the phoneNum to set
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * @return String return the backupPhoneNum
     */
    public String getBackupPhoneNum() {
        return backupPhoneNum;
    }

    /**
     * @param backupPhoneNum the backupPhoneNum to set
     */
    public void setBackupPhoneNum(String backupPhoneNum) {
        this.backupPhoneNum = backupPhoneNum;
    }

    /**
     * @return String return the staffWx
     */
    public String getStaffWx() {
        return staffWx;
    }

    /**
     * @param staffWx the staffWx to set
     */
    public void setStaffWx(String staffWx) {
        this.staffWx = staffWx;
    }

    /**
     * @return String return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return String return the lnglat
     */
    public String getLnglat() {
        return lnglat;
    }

    /**
     * @param lnglat the lnglat to set
     */
    public void setLnglat(String lnglat) {
        this.lnglat = lnglat;
    }

    /**
     * @return String return the licenseUrl
     */
    public String getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * @param licenseUrl the licenseUrl to set
     */
    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
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
     * @return double return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return double return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return double return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

}