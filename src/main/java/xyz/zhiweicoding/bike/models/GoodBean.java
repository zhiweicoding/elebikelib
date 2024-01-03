package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
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
    private double floorPrice;
    private double retailPrice;
    private double marketPrice;
    private int goodNumber;
    private String videoUrlVertical;
    private String videoUrlHorizontal;
    private String photoUrl1;
    private String photoUrl2;
    private String photoUrl3;
    private String photoUrl4;
    private String photoUrl5;
    private String tagList;
    private String symbolId;
    @TableField(value = "is_new")
    private int isNew;
    @TableField(value = "is_chosen")
    private int isChosen;
    @TableField(value = "is_cheap")
    private int isCheap;
    private int likeNum;
    private long createTime;
    private long modifyTime;
    private int isDelete;
}
