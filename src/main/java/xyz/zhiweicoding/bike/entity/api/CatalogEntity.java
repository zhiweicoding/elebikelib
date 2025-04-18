package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/1/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEntity implements Serializable {
    private List<GoodBean> goodsList = new ArrayList<>();
    private String symbolName;

    private int isPopular = -1;// 爆款
    private int isNew = -1;// 新款
    private int isChosen = -1;// 推荐
    private String order = "asc";// asc,desc
    private String symbolId = "-1";
    private int place = -1;// 1:天津,0:无锡

    public void setValueFromVo(CatalogVo vo) {
        this.isPopular = vo.getIsPopular();
        this.isNew = vo.getIsNew();
        this.isChosen = vo.getIsChosen();
        this.order = vo.getOrder();
        this.symbolId = vo.getSymbolId();
        this.place = vo.getPlace();
    }

    public List<GoodBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodBean> goodsList) {
        this.goodsList = goodsList;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(int isChosen) {
        this.isChosen = isChosen;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
