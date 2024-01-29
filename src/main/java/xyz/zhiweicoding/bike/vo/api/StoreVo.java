package xyz.zhiweicoding.bike.vo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -6255359666882098692L;

    private String cityName;
    private String searchVal;
    private String storeId;
    private double lng;
    private double lat;
}
