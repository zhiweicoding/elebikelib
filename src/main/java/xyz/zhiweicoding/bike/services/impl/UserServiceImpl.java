package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zhiweicoding.bike.dao.mysql.UserDao;
import xyz.zhiweicoding.bike.models.UserBean;
import xyz.zhiweicoding.bike.services.UserService;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserBean> implements UserService {

}




