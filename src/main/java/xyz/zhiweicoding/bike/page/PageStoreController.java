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
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.List;
import java.util.Map;

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
    public BaseResponse<AntArrayEntity<StoreBean>> index(HttpServletRequest request, String storeName, String phoneNum,
                                                         String address, int current, int pageSize) {
        try {
            Page<StoreBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<StoreBean> wrapper = Wrappers.<StoreBean>lambdaQuery().eq(StoreBean::getIsDelete, 0);
            if (storeName != null && !storeName.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(StoreBean::getStoreName, storeName)
                        .or()
                        .like(StoreBean::getStoreDesc, storeName));
            }
            if (phoneNum != null && !phoneNum.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(StoreBean::getPhoneNum, phoneNum)
                        .or()
                        .like(StoreBean::getBackupPhoneNum, phoneNum));
            }
            if (address != null && !address.isEmpty()) {
                wrapper.like(StoreBean::getAddress, address);
            }
            Page<StoreBean> pageResult = storeService.page(page, wrapper);
            AntArrayEntity<StoreBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(), pageResult.getRecords(), pageSize, (int) pageResult.getTotal());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("StoreController index error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody Map<String, Object> storeBean) {
        try {
            StoreBean store = new StoreBean();
            store.setStoreId(GeneratorUtil.getCommonId());
            store.setLicenseUrl(getUrl(storeBean, "licenseUrl"));
            store.setStoreLogo(getUrl(storeBean, "storeLogo"));
            store.setStoreName(String.valueOf(storeBean.get("storeName")));
            store.setStoreDesc(String.valueOf(storeBean.get("storeDesc")));
            store.setPhoneNum(String.valueOf(storeBean.get("phoneNum")));
            store.setBackupPhoneNum(String.valueOf(storeBean.get("backupPhoneNum")));
            store.setAddress(String.valueOf(storeBean.get("address")));
            store.setLnglat(String.valueOf(storeBean.get("lnglat")));
            store.setStaffWx(String.valueOf(storeBean.get("staffWx")));
            store.setIsDelete(0);
            store.setCreateTime(System.currentTimeMillis());
            store.setModifyTime(System.currentTimeMillis());
            storeService.save(store);
            return ResponseFactory.success(store.getStoreId());
        } catch (Exception e) {
            log.error("store save error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    private String getUrl(Map<String, Object> bean, String key) {
        String url = "";
        try {
            List<Map<String, Object>> array = (List<Map<String, Object>>) bean.get(key);
            url = String.valueOf(array.get(0).get("response"));
        } catch (Exception e) {
            log.error(" error：" + e.getMessage(), e);
        }
        return url;
    }

    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody StoreBean storeBean) {
        try {
            storeService.updateById(storeBean);
            return ResponseFactory.success(storeBean.getStoreId());
        } catch (Exception e) {
            log.error("store update error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


    @DeleteMapping("/removeList")
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
