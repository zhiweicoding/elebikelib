package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.zhiweicoding.bike.models.GoodBean;

import java.io.Serializable;
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
    private List<GoodBean> goodsList;
    private boolean categoryFilter;
    private String currentSortType;
    private String currentSortOrder;
    private List<FilterCategory> filterCategories;
    private String symbolName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterCategory implements  Serializable{
        private String id;
        private String name;
        private boolean checked;
    }
}
