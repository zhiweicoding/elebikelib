package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.EnquiryBean;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
@Mapper
@Component
public interface EnquiryDao extends BaseMapper<EnquiryBean> {
}