package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.BikeEnDao;
import xyz.zhiweicoding.bike.models.BikeEnBean;
import xyz.zhiweicoding.bike.services.BikeEnService;

/**
 * English Bike Service Implementation
 */
@Service("bikeEnService")
public class BikeEnServiceImpl extends ServiceImpl<BikeEnDao, BikeEnBean> implements BikeEnService {
}
