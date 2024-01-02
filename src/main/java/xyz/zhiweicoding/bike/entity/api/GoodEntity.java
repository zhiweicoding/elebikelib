package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 12/30/23
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodEntity implements Serializable {
    private String goodId;
    private String retailPrice;
    private String floorPrice;
    private String goodTitle;
    private String listPicUrl;
    private String scenePicUrl;
    private String goodBrief;
    private String number;
    private String typeId;
    private String symbolId;
    private String videoUrlVertical;
    private String likeNum;
    private boolean likeIt;

}
