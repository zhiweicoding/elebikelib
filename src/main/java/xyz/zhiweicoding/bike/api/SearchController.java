package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.models.BaseResponse;
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
     * 获取首页信息
     * 15 min cache
     *
     * @param params {@link SearchVo}
     * @return
     */
    @Cacheable(value = "15m", keyGenerator = "cacheJsonKeyGenerator",
            condition = "#params != null",
            unless = "#result == null || #result.isEmpty()")
    @PostMapping("/query")
    public
    @ResponseBody
    BaseResponse<IndexEntity> query(@RequestBody SearchVo params) {
        log.debug("查询页默认查询,入参 : {}", JSON.toJSONString(params));
        try {
//            IndexSendBean resultSendBean = goodService.getIndex(paramsBean);
//            log.debug("获取首页信息succee");
//            return ResponseFactory.success(resultSendBean);
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("获取首页接口报错：" + e.getMessage(), e);
//            sendBean = factory.getMsgFactory(StatusEnum.fail, null);
            return ResponseFactory.fail(null);
        }
    }


}
