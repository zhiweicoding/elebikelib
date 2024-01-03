package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.api.SearchEntity;
import xyz.zhiweicoding.bike.entity.api.SearchRedirectEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

/**
 * 首页
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/search")
@Slf4j
public class SearchController {

//    @Resource
//    private IGoodService goodService;

    /**
     * 查询页查询动作
     * 15 min cache
     *
     * @param params {@link SearchVo}
     * @return
     */
    @Cacheable(value = "15m", keyGenerator = "cacheJsonKeyGenerator", condition = "#params != null", unless = "#result == null || #result.isEmpty()")
    @PostMapping("/query")
    public BaseResponse<SearchEntity> query(@RequestBody SearchVo params) {
        log.debug("查询页默认查询,入参 : {}", JSON.toJSONString(params));
        try {
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("查询页默认查询,报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 查询页，提示信息
     * 15 min cache
     *
     * @param params {@link SearchVo}
     * @return
     */
    @Cacheable(value = "15m", keyGenerator = "cacheJsonKeyGenerator", condition = "#params != null", unless = "#result == null || #result.isEmpty()")
    @PostMapping("/help")
    public BaseResponse<SearchEntity.KeywordBean> help(@RequestBody SearchVo params) {
        log.debug("查询页，提示信息查询,入参 : {}", JSON.toJSONString(params));
        try {
            return ResponseFactory.success(null);
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
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("查询页跳转链接查询,报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
