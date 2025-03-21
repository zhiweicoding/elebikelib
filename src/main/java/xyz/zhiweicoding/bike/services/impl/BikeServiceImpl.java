package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.BikeDao;
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.services.BikeService;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Service(value = "bikeService")
@Slf4j
public class BikeServiceImpl extends ServiceImpl<BikeDao, BikeBean> implements BikeService {
}