package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.BikeEnBean;
import xyz.zhiweicoding.bike.models.BikeImageEnBean;
import xyz.zhiweicoding.bike.services.BikeEnService;
import xyz.zhiweicoding.bike.services.BikeImageEnService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Date;
import java.util.List;

/**
 * English Bike Controller
 */
@RestController
@RequestMapping(value = "/v1/page/bike-en")
@Slf4j
public class PageBikeEnController {

    @Autowired
    @Qualifier(value = "bikeEnService")
    private BikeEnService bikeEnService;

    @Autowired
    @Qualifier(value = "bikeImageEnService")
    private BikeImageEnService bikeImageEnService;

    /**
     * bike page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<BikeEnBean>> index(HttpServletRequest request,
            String title, String category,
            int current, int pageSize) {
        try {
            Page<BikeEnBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<BikeEnBean> wrapper = Wrappers.<BikeEnBean>lambdaQuery();

            if (title != null && !title.isEmpty()) {
                wrapper.like(BikeEnBean::getTitle, title);
            }
            if (category != null && !category.isEmpty()) {
                wrapper.eq(BikeEnBean::getCategory, category);
            }

            wrapper.orderByDesc(BikeEnBean::getUpdatedAt);

            Page<BikeEnBean> pageResult = bikeEnService.page(page, wrapper);

            // 为每个 BikeEnBean 加载关联的图片
            for (BikeEnBean bike : pageResult.getRecords()) {
                LambdaQueryWrapper<BikeImageEnBean> imageWrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                        .eq(BikeImageEnBean::getProductId, bike.getProductId())
                        .orderByDesc(BikeImageEnBean::getIsMain);
                List<BikeImageEnBean> images = bikeImageEnService.list(imageWrapper);
                bike.setImages(images);
            }

            AntArrayEntity<BikeEnBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(),
                    pageResult.getRecords(), pageSize, (int) pageResult.getTotal());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("英文车辆页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * save bike
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody BikeEnBean bikeEnBean) {
        try {
            Date now = new Date();
            bikeEnBean.setCreatedAt(now);
            bikeEnBean.setUpdatedAt(now);

            bikeEnService.save(bikeEnBean);

            List<BikeImageEnBean> images = bikeEnBean.getImages();
            if (images != null && !images.isEmpty()) {
                for (BikeImageEnBean image : images) {
                    image.setProductId(bikeEnBean.getProductId());
                    bikeImageEnService.save(image);
                }
            }

            return ResponseFactory.success(bikeEnBean.getProductId());
        } catch (Exception e) {
            log.error("保存英文车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * update bike
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody BikeEnBean bikeEnBean) {
        try {
            bikeEnBean.setUpdatedAt(new Date());

            bikeEnService.updateById(bikeEnBean);

            LambdaQueryWrapper<BikeImageEnBean> wrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                    .eq(BikeImageEnBean::getProductId, bikeEnBean.getProductId());
            bikeImageEnService.remove(wrapper);

            List<BikeImageEnBean> images = bikeEnBean.getImages();
            if (images != null && !images.isEmpty()) {
                for (BikeImageEnBean image : images) {
                    image.setProductId(bikeEnBean.getProductId());
                    bikeImageEnService.save(image);
                }
            }

            return ResponseFactory.success(bikeEnBean.getProductId());
        } catch (Exception e) {
            log.error("更新英文车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * delete bikes
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            bikeEnService.removeByIds(idArray);
            LambdaQueryWrapper<BikeImageEnBean> wrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                    .in(BikeImageEnBean::getProductId, idArray);
            bikeImageEnService.remove(wrapper);
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("删除英文车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * save bike image
     */
    @PostMapping("/saveImage")
    public BaseResponse<String> saveImage(HttpServletRequest request, @RequestBody BikeImageEnBean bikeImageEnBean) {
        try {
            bikeImageEnService.save(bikeImageEnBean);
            return ResponseFactory.success(bikeImageEnBean.getProductId());
        } catch (Exception e) {
            log.error("保存英文车辆图片 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * get bike images
     */
    @GetMapping("/images/{productId}")
    public BaseResponse<List<BikeImageEnBean>> getImages(HttpServletRequest request,
            @PathVariable("productId") String productId) {
        try {
            LambdaQueryWrapper<BikeImageEnBean> wrapper = Wrappers.<BikeImageEnBean>lambdaQuery()
                    .eq(BikeImageEnBean::getProductId, productId)
                    .orderByDesc(BikeImageEnBean::getIsMain);
            List<BikeImageEnBean> images = bikeImageEnService.list(wrapper);
            return ResponseFactory.success(images);
        } catch (Exception e) {
            log.error("获取英文车辆图片 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
