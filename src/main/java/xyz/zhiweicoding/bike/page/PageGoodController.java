package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.CaffeineSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.List;
import java.util.Map;

/**
 * page商品页
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/page/good")
public class PageGoodController {

    private static final Logger log = LoggerFactory.getLogger(PageGoodController.class);
    private static final String CACHE_PREFIX = "page:good:";

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    @Autowired
    private CaffeineSupport caffeineSupport;

    /**
     * good page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<GoodBean>> index(HttpServletRequest request, String symbolId,
            String goodTitle, String goodBrief,
            String isNew, String isChosen,
            int current, int pageSize) {
        try {
            // 生成缓存键
            String cacheKey = generateCacheKey(symbolId, goodTitle, goodBrief, isNew, isChosen, current, pageSize);

            // 尝试从缓存获取
            if (caffeineSupport.exists(cacheKey)) {
                String cachedResult = String.valueOf(caffeineSupport.get(cacheKey));
                if (cachedResult != null) {
                    log.debug("从缓存获取数据, key: {}", cacheKey);
                    return JSON.parseObject(cachedResult, new TypeReference<BaseResponse<AntArrayEntity<GoodBean>>>() {
                    });
                }
            }

            // 缓存未命中，查询数据库
            Page<GoodBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0);
            if (goodTitle != null && !goodTitle.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodTitle, goodTitle));
            }
            if (goodBrief != null && !goodBrief.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodBrief, goodBrief));
            }
            if (isNew != null && !isNew.isEmpty()) {
                wrapper.eq(GoodBean::getIsNew, isNew);
            }
            if (isChosen != null && !isChosen.isEmpty()) {
                wrapper.eq(GoodBean::getIsChosen, isChosen);
            }
            if (symbolId != null && !symbolId.isEmpty()) {
                wrapper.eq(GoodBean::getSymbolId, symbolId);
            }
            Page<GoodBean> pageResult = goodService.page(page, wrapper);
            AntArrayEntity<GoodBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(),
                    pageResult.getRecords(), pageSize, (int) pageResult.getTotal());

            BaseResponse<AntArrayEntity<GoodBean>> response = ResponseFactory.success(result);
            // 将结果存入缓存
            caffeineSupport.set(cacheKey, JSON.toJSONString(response));

            return response;
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);/*  */
            return ResponseFactory.fail(null);
        }
    }

    /**
     * good page save
     * 
     * @param request
     * @param goodBean
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody Map<String, Object> goodBean) {
        try {
            GoodBean good = goodService.saveGoodBean(goodBean);
            // 保存后清除缓存
            clearGoodCache();
            return ResponseFactory.success(good.getGoodId());
        } catch (Exception e) {
            log.error("save good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * good page update
     * 
     * @param request
     * @param goodBean
     * @return
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody GoodBean goodBean) {
        try {
            goodService.updateById(goodBean);
            // 更新后清除缓存
            clearGoodCache();
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("update good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * good page delete
     * 
     * @param request
     * @param idArray
     * @return
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            goodService.update(null, Wrappers.<GoodBean>lambdaUpdate()
                    .set(GoodBean::getIsDelete, -1)
                    .in(GoodBean::getGoodId, idArray));
            // 删除后清除缓存
            clearGoodCache();
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("remove good batch error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey(String symbolId, String goodTitle, String goodBrief,
            String isNew, String isChosen, int current, int pageSize) {
        return CACHE_PREFIX + "index:" +
                (symbolId == null ? "" : symbolId) + ":" +
                (goodTitle == null ? "" : goodTitle) + ":" +
                (goodBrief == null ? "" : goodBrief) + ":" +
                (isNew == null ? "" : isNew) + ":" +
                (isChosen == null ? "" : isChosen) + ":" +
                current + ":" + pageSize;
    }

    /**
     * 清除商品相关缓存
     */
    private void clearGoodCache() {
        caffeineSupport.removePattern(CACHE_PREFIX + "*");
    }

}
