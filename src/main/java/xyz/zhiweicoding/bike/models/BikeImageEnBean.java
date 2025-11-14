package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * English Bike Image Entity (bike_images_en table)
 */
@Data
@TableName("bike_images_en")
public class BikeImageEnBean {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String productId;

    private String imagePath;

    private Integer isMain;
}
