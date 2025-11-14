package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * English Bike Entity (bikes_en table)
 */
@Data
@TableName("bikes_en")
public class BikeEnBean {

    @TableId(type = IdType.ASSIGN_UUID)
    private String productId;

    private String title;

    private String category;

    private String detailHtml;

    private String prevProductId;

    private String prevProductTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    @TableField(exist = false)
    private List<BikeImageEnBean> images;
}
