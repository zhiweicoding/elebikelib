package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import xyz.zhiweicoding.bike.dao.mysql.StoreDao;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_store(门店)】的数据库操作Service实现
 * @createDate 2024-01-03 23:49:17
 */
@Service("storeService")
@Slf4j
public class StoreServiceImpl extends ServiceImpl<StoreDao, StoreBean> implements StoreService {

}




