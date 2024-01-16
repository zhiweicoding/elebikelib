package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.List;

/**
 * 门店管理
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/page/store")
@Slf4j
public class PageStoreController {

    @Autowired
    @Qualifier(value = "storeService")
    private StoreService storeService;

    /**
     * 门店查询
     */
    @PostMapping("/index")
    public BaseResponse<Page<StoreBean>> index(HttpServletRequest request, @RequestParam String anyText, @RequestParam long startTime, @RequestParam long endTime, @RequestParam int current, @RequestParam int size) {
        try {
            Page<StoreBean> page = new Page<>(current, size);
            LambdaQueryWrapper<StoreBean> wrapper = Wrappers.<StoreBean>lambdaQuery().eq(StoreBean::getIsDelete, 0);
            if (!anyText.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(StoreBean::getStoreDesc, anyText)
                        .or()
                        .like(StoreBean::getStoreName, anyText)
                        .or()
                        .like(StoreBean::getProvince, anyText)
                        .or()
                        .like(StoreBean::getCity, anyText)
                        .or()
                        .like(StoreBean::getArea, anyText)
                        .or()
                        .like(StoreBean::getPhoneNum, anyText)
                        .or()
                        .like(StoreBean::getBackupPhoneNum, anyText)
                        .or()
                        .like(StoreBean::getAddress, anyText));
            }
            if (startTime > 0 && endTime > 0) {
                wrapper.ge(StoreBean::getCreateTime, startTime);
                wrapper.le(StoreBean::getCreateTime, endTime);
            }
            Page<StoreBean> pageResult = storeService.page(page, wrapper);
            BaseResponse<Page<StoreBean>> resp = ResponseFactory.success(pageResult);
            resp.setMsgBodySize((int) pageResult.getPages());
            return resp;
        } catch (Exception e) {
            log.error("StoreController index error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody StoreBean storeBean) {
        try {
            storeBean.setStoreId(GeneratorUtil.getCommonId());
            storeService.save(storeBean);
            return ResponseFactory.success(storeBean.getStoreId());
        } catch (Exception e) {
            log.error("store save error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request,@RequestBody StoreBean storeBean) {
        try {
            storeService.updateById(storeBean);
            return ResponseFactory.success(storeBean.getStoreId());
        } catch (Exception e) {
            log.error("store update error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/remove")
    public BaseResponse<String> remove(HttpServletRequest request, @RequestParam String id) {
        try {
            storeService.update(null, Wrappers.<StoreBean>lambdaUpdate()
                    .set(StoreBean::getIsDelete, -1)
                    .eq(StoreBean::getStoreId, id));
            return ResponseFactory.success(id);
        } catch (Exception e) {
            log.error("删除store error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            storeService.update(null, Wrappers.<StoreBean>lambdaUpdate()
                    .set(StoreBean::getIsDelete, -1)
                    .in(StoreBean::getStoreId, idArray));
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("批量删除store error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
