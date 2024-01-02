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
public class SearchVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -322348262627263L;

    private String keyword;
    private String typeId;
    private String keywordId;
}
