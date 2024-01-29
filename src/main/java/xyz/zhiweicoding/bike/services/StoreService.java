package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.vo.api.StoreVo;

import java.util.List;

/**
 * @author zhiwei
 * @description 针对表【t_store(门店)】的数据库操作Service
 * @createDate 2024-01-03 23:49:17
 */
public interface StoreService extends IService<StoreBean> {


     List<StoreBean> queryStoreList(StoreVo storeVo);
}
