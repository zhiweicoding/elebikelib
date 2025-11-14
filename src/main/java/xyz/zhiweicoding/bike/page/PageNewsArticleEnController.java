package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.NewsArticleEnBean;
import xyz.zhiweicoding.bike.services.NewsArticleEnService;
import xyz.zhiweicoding.bike.support.CaffeineSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.List;
import java.util.Map;

/**
 * 英文文章Controller
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
@Slf4j
@RestController
@RequestMapping("/v1/page/news-en")
public class PageNewsArticleEnController {

    private static final String CACHE_PREFIX = "page:news-en:";

    @Autowired
    @Qualifier("newsArticleEnService")
    private NewsArticleEnService newsArticleEnService;

    @Autowired
    private CaffeineSupport caffeineSupport;

    /**
     * 分页查询英文文章
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<NewsArticleEnBean>> index(
            HttpServletRequest request,
            String title, String author,
            int current, int pageSize) {
        try {
            String cacheKey = generateCacheKey(title, author, current, pageSize);

            if (caffeineSupport.exists(cacheKey)) {
                String cachedResult = String.valueOf(caffeineSupport.get(cacheKey));
                if (cachedResult != null) {
                    log.debug("从缓存获取数据, key: {}", cacheKey);
                    return JSON.parseObject(cachedResult,
                            new TypeReference<BaseResponse<AntArrayEntity<NewsArticleEnBean>>>() {});
                }
            }

            Page<NewsArticleEnBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<NewsArticleEnBean> wrapper = Wrappers.<NewsArticleEnBean>lambdaQuery();

            if (title != null && !title.isEmpty()) {
                wrapper.like(NewsArticleEnBean::getTitle, title);
            }
            if (author != null && !author.isEmpty()) {
                wrapper.like(NewsArticleEnBean::getAuthor, author);
            }

            wrapper.orderByDesc(NewsArticleEnBean::getPublishTime);

            Page<NewsArticleEnBean> pageResult = newsArticleEnService.page(page, wrapper);
            AntArrayEntity<NewsArticleEnBean> result = new AntArrayEntity<>(
                    (int) pageResult.getCurrent(),
                    pageResult.getRecords(), 
                    pageSize, 
                    (int) pageResult.getTotal());

            BaseResponse<AntArrayEntity<NewsArticleEnBean>> response = ResponseFactory.success(result);
            caffeineSupport.set(cacheKey, JSON.toJSONString(response));

            return response;
        } catch (Exception e) {
            log.error("查询英文文章失败", e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 保存英文文章
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody NewsArticleEnBean article) {
        try {
            newsArticleEnService.saveNewsArticleEn(article);
            clearNewsCache();
            return ResponseFactory.success(article.getArticleId());
        } catch (Exception e) {
            log.error("保存英文文章失败", e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 更新英文文章
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody NewsArticleEnBean article) {
        try {
            newsArticleEnService.updateNewsArticleEn(article);
            clearNewsCache();
            return ResponseFactory.success(article.getArticleId());
        } catch (Exception e) {
            log.error("更新英文文章失败", e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 删除英文文章
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            log.info("删除英文文章 idArray：{}", idArray);
            newsArticleEnService.remove(Wrappers.<NewsArticleEnBean>lambdaQuery()
                    .in(NewsArticleEnBean::getArticleId, idArray));
            clearNewsCache();
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("删除英文文章失败", e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 翻译文章为阿拉伯语
     */
    @PostMapping("/translate-arabic")
    public BaseResponse<Object> translateArabic(
            HttpServletRequest request,
            @RequestBody Map<String, Object> payload) {
        try {
            boolean overwriteExisting = (boolean) payload.getOrDefault("overwriteExisting", false);

            // 支持单个id或多个ids
            if (payload.containsKey("id")) {
                Integer id = (Integer) payload.get("id");
                Map<String, String> result = newsArticleEnService.translateArticleById(id, overwriteExisting);
                clearNewsCache();
                return ResponseFactory.success(result);
            } else if (payload.containsKey("ids")) {
                List<Integer> ids = (List<Integer>) payload.get("ids");
                Map<String, Object> result = newsArticleEnService.translateArticlesByIds(ids, overwriteExisting);
                clearNewsCache();
                return ResponseFactory.success(result);
            } else {
                return ResponseFactory.failMsg("缺少id或ids参数");
            }
        } catch (Exception e) {
            log.error("翻译失败", e);
            return ResponseFactory.failMsg(e.getMessage());
        }
    }

    /**
     * 根据articleId查询单篇文章详情
     */
    @GetMapping("/by-article-id/{articleId}")
    public BaseResponse<NewsArticleEnBean> getByArticleId(@PathVariable String articleId) {
        try {
            NewsArticleEnBean article = newsArticleEnService.getOne(
                    Wrappers.<NewsArticleEnBean>lambdaQuery()
                            .eq(NewsArticleEnBean::getArticleId, articleId));
            if (article == null) {
                return ResponseFactory.failMsg("文章不存在");
            }
            return ResponseFactory.success(article);
        } catch (Exception e) {
            log.error("查询文章详情失败", e);
            return ResponseFactory.fail(null);
        }
    }

    private String generateCacheKey(String title, String author, int current, int pageSize) {
        return CACHE_PREFIX + "index:" +
                (title == null ? "" : title) + ":" +
                (author == null ? "" : author) + ":" +
                current + ":" + pageSize;
    }

    private void clearNewsCache() {
        caffeineSupport.removePattern(CACHE_PREFIX + "*");
    }
}
