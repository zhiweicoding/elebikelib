package xyz.zhiweicoding.bike.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

/**
 * 门店管理
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/store")
@Slf4j
public class StoreController {

    @Autowired
    @Qualifier(value = "storeService")
    private StoreService storeService;

    /**
     * 门店查询
     */
    @PostMapping("/index")
    public BaseResponse<Page<StoreBean>> index(@RequestParam String anyText, @RequestParam String startTime, @RequestParam String endTime, @RequestParam int current, @RequestParam int size) {
        try {
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("StoreController index error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
