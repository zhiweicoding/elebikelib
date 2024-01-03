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
    private String sort;
    private String order;
    private String categoryId;
}
