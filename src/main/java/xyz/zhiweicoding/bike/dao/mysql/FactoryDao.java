package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.zhiweicoding.bike.models.FactoryBean;
import org.springframework.stereotype.Component;

/**
* @author zhiwei
* @description 针对表【t_factory】的数据库操作Mapper
* @createDate 2022-03-20 15:41:26
*/
@Component
public interface FactoryDao extends BaseMapper<FactoryBean> {

}




