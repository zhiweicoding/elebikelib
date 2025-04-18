package xyz.zhiweicoding.bike.vo.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author zhiweicoding.xyz
 * @date 1/1/24
 * @email diaozhiwei2k@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
public class CatalogVo {
    private int isPopular = -1;// 爆款
    private int isNew = -1;// 新款
    private int isChosen = -1;// 推荐
    private String order = "asc";// asc,desc
    private String symbolId = "-1";
    private int place = -1;// 1:天津,0:无锡
    private String searchValue;

    /**
     * @return int return the isPopular
     */
    public int getIsPopular() {
        return isPopular;
    }

    /**
     * @param isPopular the isPopular to set
     */
    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    /**
     * @return int return the isNew
     */
    public int getIsNew() {
        return isNew;
    }

    /**
     * @param isNew the isNew to set
     */
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    /**
     * @return int return the isChosen
     */
    public int getIsChosen() {
        return isChosen;
    }

    /**
     * @param isChosen the isChosen to set
     */
    public void setIsChosen(int isChosen) {
        this.isChosen = isChosen;
    }

    /**
     * @return String return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * @return String return the symbolId
     */
    public String getSymbolId() {
        return symbolId;
    }

    /**
     * @param symbolId the symbolId to set
     */
    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    /**
     * @return int return the place
     */
    public int getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(int place) {
        this.place = place;
    }

    /**
     * @return String return the searchValue
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * @param searchValue the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

}
