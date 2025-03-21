package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.BikeImageDao;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.services.BikeImageService;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Service(value = "bikeImageService")
public class BikeImageServiceImpl extends ServiceImpl<BikeImageDao, BikeImageBean> implements BikeImageService {
    // 可以在这里实现自定义的业务方法，比如：
    // 批量保存图片
    // 更新图片主图状态
    // 获取产品的所有图片
    // 等等
}