package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class IndexEntity implements Serializable {
    private List<GoodBean> hotGoods = new ArrayList<>();
    private List<GoodBean> newGoods = new ArrayList<>();
    private List<GoodBean> brands = new ArrayList<>();
    private List<FloorGood> floorGoods = new ArrayList<>();
    private List<GoodBean> topics = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Banner implements Serializable {
        private String id;
        private String link;
        private String imageUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FloorGood implements Serializable {
        private String id;
        private String name;
        private List<GoodBean> goodsList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<GoodBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodBean> goodsList) {
            this.goodsList = goodsList;
        }
    }

    public List<GoodBean> getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(List<GoodBean> hotGoods) {
        this.hotGoods = hotGoods;
    }

    public List<GoodBean> getNewGoods() {
        return newGoods;
    }

    public void setNewGoods(List<GoodBean> newGoods) {
        this.newGoods = newGoods;
    }

    public List<GoodBean> getBrands() {
        return brands;
    }

    public void setBrands(List<GoodBean> brands) {
        this.brands = brands;
    }

    public List<FloorGood> getFloorGoods() {
        return floorGoods;
    }

    public void setFloorGoods(List<FloorGood> floorGoods) {
        this.floorGoods = floorGoods;
    }

    public List<GoodBean> getTopics() {
        return topics;
    }

    public void setTopics(List<GoodBean> topics) {
        this.topics = topics;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}
