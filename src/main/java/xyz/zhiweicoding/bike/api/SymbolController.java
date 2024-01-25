package xyz.zhiweicoding.bike.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.List;

/**
 * 电动车分类接口
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/api/symbol")
@Slf4j
public class SymbolController {

    @Autowired
    @Qualifier(value = "symbolService")
    private SymbolService symbolService;

    /**
     * 获取分类list
     * 60s
     */
    @Cacheable(value = "3m", keyGenerator = "cacheJsonKeyGenerator", condition = "#isCatalog != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/list")
    public
    @ResponseBody
    BaseResponse<List<SymbolBean>> list(boolean isCatalog) {
        try {

            List<SymbolBean> list = symbolService.list(Wrappers.<SymbolBean>lambdaQuery().select(SymbolBean::getSymbolId, SymbolBean::getPlace,
                    SymbolBean::getSymbolName));
            if (isCatalog) {
                SymbolBean all = new SymbolBean();
                all.setChecked(true);
                all.setSymbolId("-1");
                all.setSymbolName("全部");
                list.add(0, all);
            }
            log.debug("获取分类的详细信息 success");
            return ResponseFactory.success(list);
        } catch (Exception e) {
            log.error("获取分类的详细信息 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
