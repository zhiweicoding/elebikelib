package xyz.zhiweicoding.bike.vo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhiweicoding.xyz
 * @date 1/1/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogVo {
    private int isPopular = -1;//爆款
    private int isNew = -1;//新款
    private int isChosen = -1;//推荐
    private String order = "asc";//asc,desc
    private String symbolId = "-1";
    private int place = -1;//1:天津,0:无锡
}
