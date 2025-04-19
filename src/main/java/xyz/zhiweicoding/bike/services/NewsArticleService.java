package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.NewsArticleBean;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
public interface NewsArticleService extends IService<NewsArticleBean> {

    void saveNewsArticle(NewsArticleBean newsArticleBean);

    void updateNewsArticle(NewsArticleBean newsArticleBean);

}