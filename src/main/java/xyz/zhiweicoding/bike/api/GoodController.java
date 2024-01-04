package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;
import xyz.zhiweicoding.bike.vo.api.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    @PostMapping("/index")
    public BaseResponse<Page<GoodBean>> index(String anyText, int current, int size, double priceMin, double priceMax, int isNew, int isCheap, int isChosen) {
        try {
            Page<GoodBean> page = new Page<>(current, size);
            LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0);
            if (!anyText.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodTitle, anyText)
                        .or()
                        .like(GoodBean::getGoodBrief, anyText)
                        .or()
                        .like(GoodBean::getTagList, anyText));
            }
            if (priceMin > 0) {
                wrapper.ge(GoodBean::getRetailPrice, priceMin);
            }
            if (priceMax > 0) {
                wrapper.le(GoodBean::getRetailPrice, priceMax);
            }
            if (isNew != -1) {
                wrapper.eq(GoodBean::getIsNew, isNew);
            }
            if (isCheap != -1) {
                wrapper.eq(GoodBean::getIsCheap, isCheap);
            }
            if (isChosen != -1) {
                wrapper.eq(GoodBean::getIsChosen, isChosen);
            }
            Page<GoodBean> pageResult = goodService.page(page, wrapper);
            BaseResponse<Page<GoodBean>> resp = ResponseFactory.success(pageResult);
            resp.setMsgBodySize((int) pageResult.getPages());
            return resp;
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(GoodBean goodBean) {
        try {
            goodBean.setGoodId(GeneratorUtil.getCommonId());
            goodService.save(goodBean);
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("save good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/update")
    public BaseResponse<String> update(GoodBean goodBean) {
        try {
            goodService.updateById(goodBean);
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("update good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/remove")
    public BaseResponse<String> remove(@RequestParam String id) {
        try {
            goodService.update(null, Wrappers.<GoodBean>lambdaUpdate()
                    .set(GoodBean::getIsDelete, -1)
                    .eq(GoodBean::getGoodId, id));
            return ResponseFactory.success(id);
        } catch (Exception e) {
            log.error("remove good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/removeList")
    public BaseResponse<String> remove(@RequestParam List<String> idArray) {
        try {
            goodService.update(null, Wrappers.<GoodBean>lambdaUpdate()
                    .set(GoodBean::getIsDelete, -1)
                    .in(GoodBean::getGoodId, idArray));
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("remove good batch error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
