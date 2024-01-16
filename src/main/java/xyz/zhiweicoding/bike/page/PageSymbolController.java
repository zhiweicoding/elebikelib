package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.List;

/**
 * 电动车分类接口
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/page/symbol")
@Slf4j
public class PageSymbolController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    @Autowired
    @Qualifier(value = "symbolService")
    private SymbolService symbolService;

    /**
     * 分类页面查询
     *
     * @return
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<SymbolBean>> index(HttpServletRequest request, String symbolName, int current, int pageSize) {
        try {
            LambdaQueryWrapper<SymbolBean> wrapper = Wrappers.<SymbolBean>lambdaQuery()
                    .eq(SymbolBean::getIsDelete, 0)
                    .orderByAsc(SymbolBean::getSortNum);
            if (symbolName != null && !symbolName.isEmpty()) {
                wrapper.like(SymbolBean::getSymbolName, symbolName);
            }
            Page<SymbolBean> resultPage = symbolService.page(
                    new Page<>(current, pageSize),
                    wrapper
            );
            AntArrayEntity<SymbolBean> result = new AntArrayEntity<>((int) resultPage.getCurrent(), resultPage.getRecords(), pageSize);
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody SymbolBean symbolBean) {
        try {
            symbolBean.setSymbolId(GeneratorUtil.getCommonId());
            symbolService.save(symbolBean);
            return ResponseFactory.success(symbolBean.getSymbolId());
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody SymbolBean symbolBean) {
        try {
            symbolBean.setModifyTime(System.currentTimeMillis());
            symbolService.updateById(symbolBean);
            return ResponseFactory.success(symbolBean.getSymbolId());
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @DeleteMapping("/remove")
    public BaseResponse<String> remove(HttpServletRequest request, @RequestParam String id) {
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

    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
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
    public BaseResponse<String> exchangeSort(HttpServletRequest request, @RequestParam String sourceId, @RequestParam String destId) {
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
