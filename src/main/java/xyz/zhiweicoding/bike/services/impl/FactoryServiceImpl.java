package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zhiweicoding.bike.dao.mysql.FactoryDao;
import xyz.zhiweicoding.bike.models.FactoryBean;
import xyz.zhiweicoding.bike.services.FactoryService;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_factory】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service
public class FactoryServiceImpl extends ServiceImpl<FactoryDao, FactoryBean> implements FactoryService {

}




