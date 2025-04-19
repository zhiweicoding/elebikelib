package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.NewsArticleBean;
import xyz.zhiweicoding.bike.services.NewsArticleService;
import xyz.zhiweicoding.bike.support.CaffeineSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@RestController
@RequestMapping(value = "/v1/page/news")
public class PageNewsArticleController {

    private static final Logger log = LoggerFactory.getLogger(PageNewsArticleController.class);
    private static final String CACHE_PREFIX = "page:news:";

    @Autowired
    @Qualifier(value = "newsArticleService")
    private NewsArticleService newsArticleService;

    @Autowired
    private CaffeineSupport caffeineSupport;

    /**
     * news article page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<NewsArticleBean>> index(HttpServletRequest request,
            String title, String author,
            int current, int pageSize) {
        try {
            // 生成缓存键
            String cacheKey = generateCacheKey(title, author, current, pageSize);

            // 尝试从缓存获取
            if (caffeineSupport.exists(cacheKey)) {
                String cachedResult = String.valueOf(caffeineSupport.get(cacheKey));
                if (cachedResult != null) {
                    log.debug("从缓存获取数据, key: {}", cacheKey);
                    return JSON.parseObject(cachedResult,
                            new TypeReference<BaseResponse<AntArrayEntity<NewsArticleBean>>>() {
                            });
                }
            }

            Page<NewsArticleBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<NewsArticleBean> wrapper = Wrappers.<NewsArticleBean>lambdaQuery();

            if (title != null && !title.isEmpty()) {
                wrapper.like(NewsArticleBean::getTitle, title);
            }
            if (author != null && !author.isEmpty()) {
                wrapper.like(NewsArticleBean::getAuthor, author);
            }

            wrapper.orderByDesc(NewsArticleBean::getPublishTime);

            Page<NewsArticleBean> pageResult = newsArticleService.page(page, wrapper);
            AntArrayEntity<NewsArticleBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(),
                    pageResult.getRecords(), pageSize, (int) pageResult.getTotal());

            BaseResponse<AntArrayEntity<NewsArticleBean>> response = ResponseFactory.success(result);
            // 将结果存入缓存
            caffeineSupport.set(cacheKey, JSON.toJSONString(response));

            return response;
        } catch (Exception e) {
            log.error("新闻文章页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * save news article
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody NewsArticleBean newsArticleBean) {
        try {
            newsArticleService.saveNewsArticle(newsArticleBean);
            // 保存后清除缓存
            clearNewsCache();
            return ResponseFactory.success(newsArticleBean.getArticleId());
        } catch (Exception e) {
            log.error("保存新闻文章 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * update news article
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody NewsArticleBean newsArticleBean) {
        try {
            newsArticleService.updateNewsArticle(newsArticleBean);
            // 更新后清除缓存
            clearNewsCache();
            return ResponseFactory.success(newsArticleBean.getArticleId());
        } catch (Exception e) {
            log.error("更新新闻文章 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * delete news articles
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            log.info("删除新闻文章 idArray：" + idArray);
            newsArticleService.remove(Wrappers.<NewsArticleBean>lambdaQuery()
                    .in(NewsArticleBean::getArticleId, idArray));
            // 删除后清除缓存
            clearNewsCache();
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("删除新闻文章 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey(String title, String author, int current, int pageSize) {
        return CACHE_PREFIX + "index:" +
                (title == null ? "" : title) + ":" +
                (author == null ? "" : author) + ":" +
                current + ":" + pageSize;
    }

    /**
     * A清除新闻相关缓存
     */
    private void clearNewsCache() {
        caffeineSupport.removePattern(CACHE_PREFIX + "*");
    }
}