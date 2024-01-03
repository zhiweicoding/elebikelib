package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.IndexVo;

/**
 * 首页
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/home")
@Slf4j
public class HomeController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    /**
     * 获取首页信息
     * 30 min cache
     *
     * @param param {@link IndexVo}
     * @return {@link BaseResponse}
     */
    @Cacheable(value = "30m", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.isEmpty()")
    @PostMapping("/query")
    public BaseResponse<IndexEntity> query(@RequestBody IndexVo param) {
        log.debug("获取首页信息,入参 : {}", JSON.toJSONString(param));
        try {
            IndexEntity entity = goodService.getIndex(param);
            log.debug("获取首页信息,success");
            log.debug("获取首页信息,{}", entity);
            return ResponseFactory.success(entity);
        } catch (Exception e) {
            log.error("获取首页接口报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
