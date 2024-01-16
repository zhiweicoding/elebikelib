package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.models.LoginBean;

/**
* @author zhiwei
* @description 针对表【t_admin】的数据库操作Mapper
* @createDate 2022-03-20 15:41:26
*/
@Component
public interface LoginDao extends BaseMapper<LoginBean> {

}




