package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.zhiweicoding.bike.models.BikeEnBean;

/**
 * English Bike DAO
 */
@Mapper
public interface BikeEnDao extends BaseMapper<BikeEnBean> {
}
