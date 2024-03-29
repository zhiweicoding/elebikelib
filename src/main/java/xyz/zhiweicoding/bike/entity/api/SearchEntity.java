package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6996469894213879455L;

    private List<KeywordBean> helpKeywords;
    private List<KeywordBean> historyKeyword;
    private List<KeywordBean> hotKeywords;
    private DefaultKeyword defaultKeyword;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultKeyword implements Serializable {
        @Serial
        private static final long serialVersionUID = 3480658367125521023L;
        private String keyword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordBean implements Serializable {
        @Serial
        private static final long serialVersionUID = -766104949242494277L;

        private int is_hot;
        private String keyword;
        private String keywordId;

    }
}
