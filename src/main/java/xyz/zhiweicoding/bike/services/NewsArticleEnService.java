package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.NewsArticleEnBean;

import java.util.List;
import java.util.Map;

/**
 * 英文文章Service接口
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
public interface NewsArticleEnService extends IService<NewsArticleEnBean> {

    void saveNewsArticleEn(NewsArticleEnBean article);

    void updateNewsArticleEn(NewsArticleEnBean article);

    /**
     * 翻译单篇文章为阿拉伯语
     * @param id 文章ID
     * @param overwriteExisting 是否覆盖已有翻译
     * @return 翻译结果
     */
    Map<String, String> translateArticleById(Integer id, boolean overwriteExisting);

    /**
     * 批量翻译文章
     * @param ids 文章ID列表
     * @param overwriteExisting 是否覆盖已有翻译
     * @return 成功和失败的ID列表
     */
    Map<String, Object> translateArticlesByIds(List<Integer> ids, boolean overwriteExisting);
}
