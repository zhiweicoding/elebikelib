package xyz.zhiweicoding.bike.entity.api;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRedirectEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String pageUrl;
    private String keywordId;


}
