package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.zhiweicoding.bike.models.GoodBean;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<GoodBean> hotGoods = new ArrayList<>();
    private List<GoodBean> newGoods = new ArrayList<>();
    private List<GoodBean> brands = new ArrayList<>();
    private List<FloorGood> floorGoods = new ArrayList<>();
    private List<GoodBean> topics = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Banner implements Serializable {
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
        private List<GoodBean> goodsList;
    }
}
