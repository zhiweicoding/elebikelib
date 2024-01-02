package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson.JSON;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.models.BaseResponse;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品页
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/good")
@Slf4j
public class GoodController {

//    @Resource
//    private IGoodService goodService;

    /**
     * 获取详情页信息
     * 15 min cache
     *
     * @param request
     * @param paramsBean {@link IndexVo}
     * @return
     */
    @Cacheable(value = "15m", key = "#fId", unless = "#result == null")
    @PostMapping("/detail")
    public
    @ResponseBody
    BaseResponse<IndexEntity> detail(HttpServletRequest request, @RequestBody IndexVo paramsBean) {
        log.debug("获取首页信息,入参 : {}", JSON.toJSONString(paramsBean));
        try {
            log.debug("获取首页信息 success");
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("获取首页接口报错：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
