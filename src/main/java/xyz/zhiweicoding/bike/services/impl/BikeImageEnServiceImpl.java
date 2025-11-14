package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.BikeImageEnDao;
import xyz.zhiweicoding.bike.models.BikeImageEnBean;
import xyz.zhiweicoding.bike.services.BikeImageEnService;

/**
 * English Bike Image Service Implementation
 */
@Service("bikeImageEnService")
public class BikeImageEnServiceImpl extends ServiceImpl<BikeImageEnDao, BikeImageEnBean> implements BikeImageEnService {
}
