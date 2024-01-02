package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 首页返回的实体类
 *
 * @author zhiweicoding.xyz
 * @date 12/30/23
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexEntity implements Serializable {
    private List<GoodEntity> hotGoods;
    private List<GoodEntity> newGoods;
    private List<GoodEntity> brands;
    private List<FloorGood> floorGoods;
    private List<GoodEntity> topics;
    private List<Banner> banners;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Banner implements Serializable {
        private String id;
        private String link;
        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FloorGood implements Serializable {
        private String id;
        private String name;
        private List<GoodEntity> goodsList;
    }
}
