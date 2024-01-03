package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * 商品页
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/api/good")
@Slf4j
public class GoodController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    /**
     * 获取商品的详细信息
     * 15 min cache
     *
     * @param param {@link IndexVo}
     * @return
     */
    @Cacheable(value = "60s", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/detail")
    public
    @ResponseBody
    BaseResponse<GoodBean> detail(@RequestBody IndexVo param) {
        log.debug("获取商品的详细信息,入参 : {}", JSON.toJSONString(param));
        try {
            GoodBean detail = goodService.getOne(Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getGoodId, param.getGoodId()));
            log.debug("获取商品的详细信息 success");
            return ResponseFactory.success(detail);
        } catch (Exception e) {
            log.error("获取商品的详细信息 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
