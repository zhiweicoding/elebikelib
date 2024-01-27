package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.AdviceDao;
import xyz.zhiweicoding.bike.models.AdviceBean;
import xyz.zhiweicoding.bike.services.AdviceService;

/**
 * @author zhiwei
 * @description 针对表【t_config】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "adviceService")
@Slf4j
public class AdviceServiceImpl extends ServiceImpl<AdviceDao, AdviceBean> implements AdviceService {

}




