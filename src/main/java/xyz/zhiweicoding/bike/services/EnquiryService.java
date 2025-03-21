package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.EnquiryBean;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
public interface EnquiryService extends IService<EnquiryBean> {
    // 可以在这里添加自定义的业务方法，比如：
    // 更新询盘状态
    // 获取未处理的询盘列表
    // 按时间范围统计询盘数量
    // 等等
}