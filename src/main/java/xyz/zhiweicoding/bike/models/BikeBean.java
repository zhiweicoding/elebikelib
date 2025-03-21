package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bikes")
public class BikeBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String productId;
    private String title;
    private String category;
    private String detailHtml;
    private String prevProductId;
    private String prevProductTitle;
    private Date createdAt;
    private Date updatedAt;
}