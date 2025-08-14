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
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.BikeImageService;
import xyz.zhiweicoding.bike.services.BikeService;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.CaffeineSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Date;
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

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BikeImageService bikeImageService;

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

            // 同时更新对应的bike记录
            updateRelatedBikeData(goodBean);

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

            // 同时删除对应的bike和bikeimg记录
            deleteRelatedBikeData(idArray);

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

    /**
     * 更新相关的bike和bikeimg数据
     *
     * @param goodBean 商品信息
     */
    private void updateRelatedBikeData(GoodBean goodBean) {
        try {
            // 根据goodId生成对应的productId (去掉"g"前缀，加上"p"前缀)
            String productId = "p" + goodBean.getGoodId().substring(1);

            // 生成detail_html内容
            String detailHtml = generateDetailHtml(goodBean.getPhotoUrl());

            // 更新bike记录
            bikeService.update(null, Wrappers.<BikeBean>lambdaUpdate()
                    .set(BikeBean::getTitle, goodBean.getGoodTitle())
                    .set(BikeBean::getCategory, goodBean.getPcSymbolId())
                    .set(BikeBean::getDetailHtml, detailHtml)
                    .set(BikeBean::getUpdatedAt, new Date())
                    .eq(BikeBean::getProductId, productId));

            // 更新bikeimg记录 - 更新主图
            bikeImageService.update(null, Wrappers.<BikeImageBean>lambdaUpdate()
                    .set(BikeImageBean::getImagePath, goodBean.getListPicUrl())
                    .eq(BikeImageBean::getProductId, productId)
                    .eq(BikeImageBean::getIsMain, 1));

            log.debug("Updated related bike data for goodId: {}", goodBean.getGoodId());
        } catch (Exception e) {
            log.error("Failed to update related bike data for goodId: " + goodBean.getGoodId(), e);
        }
    }

    /**
     * 删除相关的bike和bikeimg数据
     *
     * @param goodIdArray 商品ID数组
     */
    private void deleteRelatedBikeData(List<String> goodIdArray) {
        try {
            for (String goodId : goodIdArray) {
                // 根据goodId生成对应的productId
                String productId = "p" + goodId.substring(1);
                String imageProductId = "i" + goodId.substring(1);

                // 删除bike记录 (软删除或硬删除，根据业务需求)
                bikeService.remove(Wrappers.<BikeBean>lambdaQuery()
                        .eq(BikeBean::getProductId, productId));

                // 删除bikeimg记录
                bikeImageService.remove(Wrappers.<BikeImageBean>lambdaQuery()
                        .eq(BikeImageBean::getProductId, imageProductId));
            }

            log.debug("Deleted related bike data for goodIds: {}", goodIdArray);
        } catch (Exception e) {
            log.error("Failed to delete related bike data for goodIds: " + goodIdArray, e);
        }
    }

    /**
     * 生成detail_html内容
     * 将photoUrl数组转换为HTML格式
     *
     * @param photoUrl JSON格式的图片URL数组
     * @return HTML格式的详情内容
     */
    private String generateDetailHtml(String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            return "<p><br/></p>";
        }

        try {
            // 解析JSON数组
            List<String> photoUrlList = JSON.parseArray(photoUrl, String.class);
            if (photoUrlList == null || photoUrlList.isEmpty()) {
                return "<p><br/></p>";
            }

            StringBuilder detailHtmlBuilder = new StringBuilder();

            // 为每个图片URL生成HTML段落
            for (String imageUrl : photoUrlList) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    detailHtmlBuilder.append("<p style=\"text-align: center;\">")
                            .append("<img data-src=\"").append(imageUrl).append("\" ")
                            .append("src=\"https://bodocn-1256485110.cos.ap-beijing.myqcloud.com/images/imgbg.png\" ")
                            .append("style=\"\"/>")
                            .append("</p>")
                            .append("<p><br/></p>");
                }
            }

            return detailHtmlBuilder.toString();
        } catch (Exception e) {
            log.error("Failed to generate detail HTML from photoUrl: " + photoUrl, e);
            return "<p><br/></p>";
        }
    }

}
