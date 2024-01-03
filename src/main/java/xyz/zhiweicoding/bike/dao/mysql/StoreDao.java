package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.StoreBean;

/**
 * @author zhiwei
 * @description 针对表【t_store(门店)】的数据库操作Mapper
 * @createDate 2024-01-03 23:49:17
 * @Entity xyz.zhiweicoding.bike.models.TStoreMapper
 */
@Component
public interface StoreDao extends BaseMapper<StoreBean> {

}




