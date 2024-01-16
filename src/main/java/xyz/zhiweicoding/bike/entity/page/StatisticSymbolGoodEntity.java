package xyz.zhiweicoding.bike.entity.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/13/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticSymbolGoodEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3560552058079056540L;

    /**
     * type : 分类一
     * value : 27
     */

    private String type;
    private long value;

}
