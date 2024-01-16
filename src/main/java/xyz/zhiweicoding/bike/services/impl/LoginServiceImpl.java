package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.LoginDao;
import xyz.zhiweicoding.bike.models.LoginBean;
import xyz.zhiweicoding.bike.services.LoginService;

/**
 * @author zhiwei
 * @description 针对表【t_admin】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "loginService")
@Slf4j
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginBean> implements LoginService {

}




