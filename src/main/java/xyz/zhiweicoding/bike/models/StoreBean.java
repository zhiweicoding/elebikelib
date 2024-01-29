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

/**
 * 门店
 *
 * @TableName t_store
 */
@Data
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

}