package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.StoreVo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 商品页
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/api/store")
@Slf4j
public class StoreController {

    @Autowired
    @Qualifier(value = "storeService")
    private StoreService storeService;

    /**
     * 获取门店列表
     *
     * @param param {@link StoreVo}
     * @return {@link List<StoreBean>}
     */
    @PostMapping("/list")
    @Cacheable(value = "30s", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    public BaseResponse<List<StoreBean>> list(@RequestBody StoreVo param) {
        try {
            List<StoreBean> storeBeans = storeService.queryStoreList(param);
            return ResponseFactory.success(storeBeans);
        } catch (Exception e) {
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 获取门店详情
     *
     * @param param {@link StoreVo}
     * @return {@link StoreBean}
     */
    @PostMapping("/detail")
    @Cacheable(value = "3m", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    public BaseResponse<StoreBean> detail(@RequestBody StoreVo param) {
        try {
            StoreBean one = storeService.getOne(Wrappers.<StoreBean>lambdaQuery()
                    .eq(StoreBean::getIsDelete, 0)
                    .eq(StoreBean::getStoreId, param.getStoreId()));
            return ResponseFactory.success(one);
        } catch (Exception e) {
            return ResponseFactory.fail(null);
        }
    }


}
