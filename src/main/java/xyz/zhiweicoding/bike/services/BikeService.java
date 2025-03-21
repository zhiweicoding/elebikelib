package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.BikeBean;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
public interface BikeService extends IService<BikeBean> {
    // 可以在这里添加自定义的业务方法，比如：
    // 获取推荐车型列表
    // 获取相关车型
    // 更新车型详情
    // 统计各类别车型数量
    // 等等
}