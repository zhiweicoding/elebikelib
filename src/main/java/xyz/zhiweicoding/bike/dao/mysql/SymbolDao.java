package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;

import java.util.List;

/**
 * @author zhiwei
 * @description 针对表【t_symbol】的数据库操作Mapper
 * @createDate 2022-03-20 15:41:26
 */
@Component
public interface SymbolDao extends BaseMapper<SymbolBean> {

}




