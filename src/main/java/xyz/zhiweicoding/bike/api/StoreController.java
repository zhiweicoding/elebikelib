package xyz.zhiweicoding.bike.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

import java.util.List;

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
    @Cacheable(value = "3m", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    public BaseResponse<List<StoreBean>> list(@RequestBody StoreVo param) {
        try {
            List<StoreBean> list = storeService.list(Wrappers.<StoreBean>lambdaQuery()
                    .eq(StoreBean::getIsDelete, 0)
                    .like(StoreBean::getAddress, param.getCityName())
                    .like(StoreBean::getStoreName, param.getSearchVal()));
            return ResponseFactory.success(list);
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
