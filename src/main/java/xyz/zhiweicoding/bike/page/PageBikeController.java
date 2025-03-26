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
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.services.BikeImageService;
import xyz.zhiweicoding.bike.services.BikeService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Date;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@RestController
@RequestMapping(value = "/v1/page/bike")
@Slf4j
public class PageBikeController {

    @Autowired
    @Qualifier(value = "bikeService")
    private BikeService bikeService;

    @Autowired
    @Qualifier(value = "bikeImageService")
    private BikeImageService bikeImageService;

    /**
     * bike page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<BikeBean>> index(HttpServletRequest request,
            String title, String category,
            int current, int pageSize) {
        try {
            Page<BikeBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<BikeBean> wrapper = Wrappers.<BikeBean>lambdaQuery();

            if (title != null && !title.isEmpty()) {
                wrapper.like(BikeBean::getTitle, title);
            }
            if (category != null && !category.isEmpty()) {
                wrapper.eq(BikeBean::getCategory, category);
            }

            wrapper.orderByDesc(BikeBean::getUpdatedAt);

            Page<BikeBean> pageResult = bikeService.page(page, wrapper);

            // 为每个 BikeBean 加载关联的图片
            for (BikeBean bike : pageResult.getRecords()) {
                LambdaQueryWrapper<BikeImageBean> imageWrapper = Wrappers.<BikeImageBean>lambdaQuery()
                        .eq(BikeImageBean::getProductId, bike.getProductId())
                        .orderByDesc(BikeImageBean::getIsMain);
                List<BikeImageBean> images = bikeImageService.list(imageWrapper);
                bike.setImages(images);
            }

            AntArrayEntity<BikeBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(),
                    pageResult.getRecords(), pageSize, (int) pageResult.getTotal());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("车辆页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * save bike
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody BikeBean bikeBean) {
        try {
            Date now = new Date();
            bikeBean.setCreatedAt(now);
            bikeBean.setUpdatedAt(now);

            // 先保存自行车基本信息
            bikeService.save(bikeBean);

            // 保存关联的图片信息
            List<BikeImageBean> images = bikeBean.getImages();
            if (images != null && !images.isEmpty()) {
                for (BikeImageBean image : images) {
                    image.setProductId(bikeBean.getProductId());
                    bikeImageService.save(image);
                }
            }

            return ResponseFactory.success(bikeBean.getProductId());
        } catch (Exception e) {
            log.error("保存车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * update bike
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody BikeBean bikeBean) {
        try {
            bikeBean.setUpdatedAt(new Date());

            // 更新自行车基本信息
            bikeService.updateById(bikeBean);

            // 更新图片信息：先删除原有图片，再保存新图片
            LambdaQueryWrapper<BikeImageBean> wrapper = Wrappers.<BikeImageBean>lambdaQuery()
                    .eq(BikeImageBean::getProductId, bikeBean.getProductId());
            bikeImageService.remove(wrapper);

            List<BikeImageBean> images = bikeBean.getImages();
            if (images != null && !images.isEmpty()) {
                for (BikeImageBean image : images) {
                    image.setProductId(bikeBean.getProductId());
                    bikeImageService.save(image);
                }
            }

            return ResponseFactory.success(bikeBean.getProductId());
        } catch (Exception e) {
            log.error("更新车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * delete bikes
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            bikeService.removeByIds(idArray);
            // Also remove related images
            LambdaQueryWrapper<BikeImageBean> wrapper = Wrappers.<BikeImageBean>lambdaQuery()
                    .in(BikeImageBean::getProductId, idArray);
            bikeImageService.remove(wrapper);
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("删除车辆 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * save bike image
     */
    @PostMapping("/saveImage")
    public BaseResponse<String> saveImage(HttpServletRequest request, @RequestBody BikeImageBean bikeImageBean) {
        try {
            bikeImageService.save(bikeImageBean);
            return ResponseFactory.success(bikeImageBean.getProductId());
        } catch (Exception e) {
            log.error("保存车辆图片 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * get bike images
     */
    @GetMapping("/images/{productId}")
    public BaseResponse<List<BikeImageBean>> getImages(HttpServletRequest request,
            @PathVariable("productId") String productId) {
        try {
            LambdaQueryWrapper<BikeImageBean> wrapper = Wrappers.<BikeImageBean>lambdaQuery()
                    .eq(BikeImageBean::getProductId, productId)
                    .orderByDesc(BikeImageBean::getIsMain);
            List<BikeImageBean> images = bikeImageService.list(wrapper);
            return ResponseFactory.success(images);
        } catch (Exception e) {
            log.error("获取车辆图片 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}