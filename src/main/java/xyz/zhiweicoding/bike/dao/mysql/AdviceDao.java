package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.AdviceBean;

/**
 * @author zhiwei
 * @description 针对表【t_advice(意见表)】的数据库操作Mapper
 * @createDate 2022-03-20 15:41:26
 */
@Component
public interface AdviceDao extends BaseMapper<AdviceBean> {
}




