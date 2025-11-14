package xyz.zhiweicoding.bike.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.BikeEnBean;
import xyz.zhiweicoding.bike.models.BikeImageEnBean;
import xyz.zhiweicoding.bike.services.BikeEnService;
import xyz.zhiweicoding.bike.services.BikeImageEnService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Public API for English Bikes (Frontend Website)
 */
@RestController
@RequestMapping(value = "/v1/en/bikes")
@Slf4j
public class BikeEnController {

    @Autowired
    @Qualifier(value = "bikeEnService")
    private BikeEnService bikeEnService;

    @Autowired
    @Qualifier(value = "bikeImageEnService")
    private BikeImageEnService bikeImageEnService;

    /**
     * Get bikes by category with pagination
     * POST /v1/en/bikes/{category}/{page}/{pageSize}
     */
    @PostMapping("/{category}/{page}/{pageSize}")
    public BaseResponse<Map<String, Object>> getBikesByCategory(
            HttpServletRequest request,
            @PathVariable("category") String category,
            @PathVariable("page") int page,
            @PathVariable("pageSize") int pageSize,
            @RequestBody(required = false) Map<String, Object> requestBody) {
        try {
            Page<BikeEnBean> bikePage = new Page<>(page, pageSize);
            LambdaQueryWrapper<BikeEnBean> wrapper = Wrappers.<BikeEnBean>lambdaQuery();

            if (category != null && !category.isEmpty()) {
                wrapper.eq(BikeEnBean::getCategory, category);
            }

            wrapper.orderByDesc(BikeEnBean::getUpdatedAt);

            Page<BikeEnBean> pageResult = bikeEnService.page(bikePage, wrapper);

            // Load images for each bike
            for (BikeEnBean bike : pageResult.getRecords()) {
                LambdaQueryWrapper<BikeImageEnBean> imageWrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                        .eq(BikeImageEnBean::getProductId, bike.getProductId())
                        .orderByDesc(BikeImageEnBean::getIsMain);
                List<BikeImageEnBean> images = bikeImageEnService.list(imageWrapper);
                bike.setImages(images);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("bikes", pageResult.getRecords());
            result.put("page", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());
            result.put("totalPages", pageResult.getPages());
            result.put("totalRecords", pageResult.getTotal());

            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("获取英文车辆分类数据失败: " + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * Get bike detail by productId
     * GET /v1/en/bikes/detail/{productId}
     */
    @GetMapping("/detail/{productId}")
    public BaseResponse<BikeEnBean> getBikeDetail(
            HttpServletRequest request,
            @PathVariable("productId") String productId) {
        try {
            BikeEnBean bike = bikeEnService.getById(productId);

            if (bike == null) {
                return ResponseFactory.failMsg("Bike not found");
            }

            // Load images
            LambdaQueryWrapper<BikeImageEnBean> imageWrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                    .eq(BikeImageEnBean::getProductId, productId)
                    .orderByDesc(BikeImageEnBean::getIsMain);
            List<BikeImageEnBean> images = bikeImageEnService.list(imageWrapper);
            bike.setImages(images);

            return ResponseFactory.success(bike);
        } catch (Exception e) {
            log.error("获取英文车辆详情失败: " + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * Search bikes by keyword
     * GET /v1/en/bikes/search
     */
    @GetMapping("/search")
    public BaseResponse<Map<String, Object>> searchBikes(
            HttpServletRequest request,
            @RequestParam("q") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            Page<BikeEnBean> bikePage = new Page<>(page, size);
            LambdaQueryWrapper<BikeEnBean> wrapper = Wrappers.<BikeEnBean>lambdaQuery();

            if (keyword != null && !keyword.isEmpty()) {
                wrapper.like(BikeEnBean::getTitle, keyword)
                        .or()
                        .like(BikeEnBean::getCategory, keyword);
            }

            wrapper.orderByDesc(BikeEnBean::getUpdatedAt);

            Page<BikeEnBean> pageResult = bikeEnService.page(bikePage, wrapper);

            // Load images
            for (BikeEnBean bike : pageResult.getRecords()) {
                LambdaQueryWrapper<BikeImageEnBean> imageWrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                        .eq(BikeImageEnBean::getProductId, bike.getProductId())
                        .orderByDesc(BikeImageEnBean::getIsMain);
                List<BikeImageEnBean> images = bikeImageEnService.list(imageWrapper);
                bike.setImages(images);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("bikes", pageResult.getRecords());
            result.put("page", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());
            result.put("totalPages", pageResult.getPages());
            result.put("totalRecords", pageResult.getTotal());

            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("搜索英文车辆失败: " + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
