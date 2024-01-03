package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;
import xyz.zhiweicoding.bike.vo.api.IndexVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 电动车分类接口
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/page/symbol")
@Slf4j
public class SymbolPageController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    @Autowired
    @Qualifier(value = "symbolService")
    private SymbolService symbolService;

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

    /**
     * 分类页面查询
     *
     * @param anyText {@link String}
     * @return
     */
    @Cacheable(value = "30s", keyGenerator = "cacheJsonKeyGenerator", condition = "#anyText != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/index")
    public BaseResponse<List<SymbolBean>> index(String anyText) {
        try {
            LambdaQueryWrapper<SymbolBean> wrapper = Wrappers.<SymbolBean>lambdaQuery()
                    .eq(SymbolBean::getIsDelete, 0);
            if (!anyText.isEmpty()) {
                wrapper.like(SymbolBean::getSymbolName, anyText);
            }
            List<SymbolBean> resultList = symbolService.list(wrapper);
            BaseResponse<List<SymbolBean>> resp = ResponseFactory.success(resultList);
            resp.setMsgBodySize(resultList.size());
            return resp;
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(SymbolBean symbolBean) {
        try {
            symbolBean.setSymbolId(GeneratorUtil.getCommonId());
            symbolService.save(symbolBean);
            return ResponseFactory.success(symbolBean.getSymbolId());
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/update")
    public BaseResponse<String> update(SymbolBean symbolBean) {
        try {
            symbolService.update(symbolBean, Wrappers.<SymbolBean>lambdaUpdate().eq(SymbolBean::getSymbolId, symbolBean.getSymbolId()));
            return ResponseFactory.success(symbolBean.getSymbolId());
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/remove")
    public BaseResponse<String> remove(@RequestParam String id) {
        try {
            symbolService.update(null, Wrappers.<SymbolBean>lambdaUpdate()
                    .set(SymbolBean::getIsDelete, -1)
                    .eq(SymbolBean::getSymbolId, id));
            return ResponseFactory.success(id);
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/removeList")
    public BaseResponse<String> remove(@RequestParam List<String> idArray) {
        try {
            symbolService.update(null, Wrappers.<SymbolBean>lambdaUpdate()
                    .set(SymbolBean::getIsDelete, -1)
                    .in(SymbolBean::getSymbolId, idArray));
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/exchangeSort")
    public BaseResponse<String> exchangeSort(@RequestParam String sourceId, @RequestParam String destId) {
        try {
            SymbolBean source = symbolService.getById(sourceId);
            SymbolBean dest = symbolService.getById(destId);

            symbolService.update(null, Wrappers.<SymbolBean>lambdaUpdate()
                    .set(SymbolBean::getSortNum, source.getSortNum())
                    .eq(SymbolBean::getSymbolId, destId));
            symbolService.update(null, Wrappers.<SymbolBean>lambdaUpdate()
                    .set(SymbolBean::getSortNum, dest.getSortNum())
                    .eq(SymbolBean::getSymbolId, sourceId));
            return ResponseFactory.success("change");
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
