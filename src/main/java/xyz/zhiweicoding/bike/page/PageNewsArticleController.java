package xyz.zhiweicoding.bike.page;

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
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.Date;
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

    @Autowired
    @Qualifier(value = "newsArticleService")
    private NewsArticleService newsArticleService;

    /**
     * news article page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<NewsArticleBean>> index(HttpServletRequest request,
            String title, String author,
            int current, int pageSize) {
        try {
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
            return ResponseFactory.success(result);
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
            newsArticleBean.setArticleId(GeneratorUtil.getCommonId());
            newsArticleBean.setHits(0);
            newsArticleBean.setCreatedAt(new Date());
            newsArticleService.save(newsArticleBean);
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
            newsArticleService.updateById(newsArticleBean);
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
            newsArticleService.removeByIds(idArray);
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("删除新闻文章 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}