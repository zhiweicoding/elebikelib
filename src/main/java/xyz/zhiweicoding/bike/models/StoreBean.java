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
    /**
     *
     */
    @TableId(value = "store_id", type = IdType.ASSIGN_UUID)
    private String store_id;

    /**
     *
     */
    @TableField(value = "store_name")
    private String store_name;

    /**
     * 详细信息
     */
    @TableField(value = "store_desc")
    private String store_desc;

    /**
     *
     */
    @TableField(value = "store_logo")
    private String store_logo;

    /**
     * 电话
     */
    @TableField(value = "phone_num")
    private String phone_num;

    /**
     * 备用电话
     */
    @TableField(value = "backup_phone_num")
    private String backup_phone_num;

    /**
     * 企业微信链接
     */
    @TableField(value = "staff_wx")
    private String staff_wx;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     *
     */
    @TableField(value = "city")
    private String city;

    /**
     * 区县
     */
    @TableField(value = "area")
    private String area;

    /**
     *
     */
    @TableField(value = "longitude")
    private double longitude;

    /**
     *
     */
    @TableField(value = "latitude")
    private double latitude;

    /**
     * 营业执照等的链接
     */
    @TableField(value = "license_url")
    private String license_url;

    /**
     *
     */
    @TableField(value = "is_delete")
    private int is_delete;

    /**
     *
     */
    @TableField(value = "create_time")
    private long create_time;

    /**
     *
     */
    @TableField(value = "modify_time")
    private long modify_time;

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
}