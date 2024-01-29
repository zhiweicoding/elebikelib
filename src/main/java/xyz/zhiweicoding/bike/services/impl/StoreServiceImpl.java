package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.zhiweicoding.bike.dao.mysql.StoreDao;
import xyz.zhiweicoding.bike.models.StoreBean;
import xyz.zhiweicoding.bike.services.StoreService;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.vo.api.StoreVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiwei
 * @description 针对表【t_store(门店)】的数据库操作Service实现
 * @createDate 2024-01-03 23:49:17
 */
@Service("storeService")
@Slf4j
public class StoreServiceImpl extends ServiceImpl<StoreDao, StoreBean> implements StoreService {


    @Override
    public List<StoreBean> queryStoreList(StoreVo storeVo) {
        LambdaQueryWrapper<StoreBean> wrapper = Wrappers.<StoreBean>lambdaQuery().eq(StoreBean::getIsDelete, 0);
        if (storeVo.getCityName() != null && !storeVo.getCityName().isEmpty() && !storeVo.getCityName().equals("全国")) {
            wrapper.like(StoreBean::getAddress, storeVo.getCityName());
        }
        if (storeVo.getSearchVal() != null && !storeVo.getSearchVal().isEmpty()) {
            wrapper.like(StoreBean::getStoreName, storeVo.getSearchVal());
        }
        List<StoreBean> list = baseMapper.selectList(wrapper);

        if (storeVo.getLat() > 0 && storeVo.getLng() > 0) {
            double lat = storeVo.getLat();
            double lng = storeVo.getLng();
            return list.stream().peek((bean) -> {
                String lnglat1 = bean.getLnglat();
                if (lnglat1.contains(",")) {
                    String[] split = lnglat1.split(",");
                    bean.setLat(Double.parseDouble(split[0]));
                    bean.setLng(Double.parseDouble(split[1]));
                }
                bean.setDistance(isRange(lat, lng, bean.getLat(), bean.getLng()));
            }).sorted(Comparator.comparingDouble(StoreBean::getDistance)).toList();
        } else {
            return list;
        }
    }

    private double isRange(double startLat, double startLong, double endLat, double endLong) {
        double latDistance = Math.toRadians(endLat - startLat);
        double lonDistance = Math.toRadians(endLong - startLong);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = 6371 * c;
        return new BigDecimal(distance).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}




