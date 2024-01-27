package xyz.zhiweicoding.bike.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.AdviceBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.AdviceService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

import java.util.List;
import java.util.Map;

/**
 * 上报问题
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/api/advice")
@Slf4j
public class AdviceController {

    @Autowired
    @Qualifier(value = "adviceService")
    private AdviceService adviceService;

    /**
     * 报告意见
     */
    @PostMapping("/save")
    public
    @ResponseBody
    BaseResponse<Boolean> save(@RequestBody Map<String, String> param) {
        try {
            AdviceBean adviceBean = new AdviceBean();
            adviceBean.setAdviceName(param.get("msg"));
            adviceBean.setCreateTime(System.currentTimeMillis());
            adviceBean.setModifyTime(System.currentTimeMillis());
            adviceBean.setIsDelete(0);
            adviceService.save(adviceBean);
            return ResponseFactory.success(true);
        } catch (Exception e) {
            log.error("报告意见 error：" + e.getMessage(), e);
            return ResponseFactory.fail(false);
        }
    }


}
