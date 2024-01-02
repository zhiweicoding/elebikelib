package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.models.BaseResponse;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

/**
 * 目录页
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/catalog")
@Slf4j
public class CatalogController {

//    @Resource
//    private IGoodService goodService;

    /**
     * 获取目录页的信息
     * 15 min cache
     *
     * @param params {@link CatalogVo}
     * @return
     */
    @Cacheable(value = "15m", key = "#fId", unless = "#result == null")
    @PostMapping("/query")
    public
    @ResponseBody
    BaseResponse<CatalogEntity> query(@RequestBody CatalogVo params) {
        log.debug("获取目录页的信息,入参 : {}", JSON.toJSONString(params));
        try {
            log.debug("获取目录页的信息 success");
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("获取首页接口报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
