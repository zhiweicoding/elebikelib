package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.page.StatisticSymbolGoodEntity;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.StoreService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析页面数据来源
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/page/statistic")
@Slf4j
public class PageStatisticController {

    @SuppressWarnings("all")
    @Autowired
    @Qualifier(value = "symbolService")
    private SymbolService symbolService;
    @SuppressWarnings("all")
    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;
    @SuppressWarnings("all")
    @Autowired
    @Qualifier(value = "storeService")
    private StoreService storeService;


    /**
     * statistic common query
     */
    @PostMapping("/common")
    public BaseResponse<Map<String, Long>> common(HttpServletRequest request) {
        try {
            long symbolCount = symbolService.count(Wrappers.<SymbolBean>lambdaQuery().eq(SymbolBean::getIsDelete, 0));
            long goodCount = goodService.count(Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0));
            long storeCount = storeService.count(Wrappers.<StoreBean>lambdaQuery().eq(StoreBean::getIsDelete, 0));
            Map<String, Long> map = Map.of("symbol", symbolCount, "good", goodCount, "store", storeCount);
            return ResponseFactory.success(map);
        } catch (Exception e) {
            log.error("login q error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * statistic page division good group by symbol
     */
    @PostMapping("/symbolGood")
    public BaseResponse<List<StatisticSymbolGoodEntity>> symbolGood(HttpServletRequest request) {
        try {
            List<StatisticSymbolGoodEntity> resultList = new ArrayList<>();
            List<SymbolBean> symbolArray = symbolService.list(Wrappers.<SymbolBean>lambdaQuery().eq(SymbolBean::getIsDelete, 0).select(SymbolBean::getSymbolName, SymbolBean::getSymbolId));
            for (SymbolBean sb : symbolArray) {
                StatisticSymbolGoodEntity entity = new StatisticSymbolGoodEntity();
                long tempCount = goodService.count(Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0).eq(GoodBean::getSymbolId, sb.getSymbolId()));
                entity.setType(sb.getSymbolName());
                entity.setValue(tempCount);
                resultList.add(entity);
            }
            return ResponseFactory.success(resultList);
        } catch (Exception e) {
            log.error("login q error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
