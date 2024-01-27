package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.SearchEntity;
import xyz.zhiweicoding.bike.entity.api.SearchRedirectEntity;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.SearchService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

import java.util.List;

/**
 * 搜索
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/search")
@Slf4j
@Deprecated
public class SearchController {

    @Autowired
    @Qualifier(value = "searchService")
    private SearchService searchService;

    /**
     * 查询页查询动作
     * 15 min cache
     *
     * @param timestamp {@link String}
     * @return
     */
    @Cacheable(value = "15m", keyGenerator = "cacheJsonKeyGenerator", condition = "#timestamp != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/query")
    public BaseResponse<SearchEntity> query(@RequestParam String timestamp) {
        try {
            SearchEntity search = searchService.getSearch();
            return ResponseFactory.success(search);
        } catch (Exception e) {
            log.error("查询页默认查询,报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 查询页，提示信息
     * 15 min cache
     *
     * @param param {@link SearchVo}
     * @return
     */
    @Cacheable(value = "15m", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/help")
    public BaseResponse<List<SearchEntity.KeywordBean>> help(@RequestBody SearchVo param) {
        log.debug("查询页，提示信息查询,入参 : {}", JSON.toJSONString(param));
        try {
            List<SearchEntity.KeywordBean> keywordList = searchService.getKeywordList(param);
            return ResponseFactory.success(keywordList);
        } catch (Exception e) {
            log.error("查询页，提示信息查询,报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 查询页跳转链接
     *
     * @param params {@link SearchVo}
     * @return
     */
    @PostMapping("/redirect")
    public BaseResponse<SearchRedirectEntity> redirect(@RequestBody SearchVo params) {
        log.debug("查询页跳转链接查询,入参 : {}", JSON.toJSONString(params));
        try {
            return searchService.getRedirect(params);
        } catch (Exception e) {
            log.error("查询页跳转链接查询,报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
