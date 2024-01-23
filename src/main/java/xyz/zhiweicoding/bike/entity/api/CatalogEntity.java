package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/1/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEntity implements Serializable {
    private List<GoodBean> goodsList = new ArrayList<>();
    private String symbolName;

    private int isPopular = -1;//爆款
    private int isNew = -1;//新款
    private int isChosen = -1;//推荐
    private String order = "asc";//asc,desc
    private String symbolId = "-1";
    private int place = -1;//1:天津,0:无锡


    public void setValueFromVo(CatalogVo vo) {
        this.isPopular = vo.getIsPopular();
        this.isNew = vo.getIsNew();
        this.isChosen = vo.getIsChosen();
        this.order = vo.getOrder();
        this.symbolId = vo.getSymbolId();
        this.place = vo.getPlace();
    }
}
