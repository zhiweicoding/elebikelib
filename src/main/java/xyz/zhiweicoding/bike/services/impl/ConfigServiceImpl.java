package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zhiweicoding.bike.dao.mysql.ConfigDao;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.services.ConfigService;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_config(配置表)】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigBean> implements ConfigService {

}




