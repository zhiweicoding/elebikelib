package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.AdminConsoleUserEntity;
import xyz.zhiweicoding.bike.models.LoginBean;
import xyz.zhiweicoding.bike.services.LoginService;
import xyz.zhiweicoding.bike.support.RedisSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Map;

/**
 * page login query
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/page/user")
@Slf4j
public class PageUserController {

    @SuppressWarnings("all")
    @Autowired
    @Qualifier(value = "loginService")
    private LoginService loginService;

    @SuppressWarnings("all")
    @Autowired
    private RedisSupport redisSupport;


    /**
     * query
     */
    @PostMapping("/query")
    public BaseResponse<AdminConsoleUserEntity> query(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.debug("login q,入参 : {}", authorization);
        try {

            String value = String.valueOf(redisSupport.get(authorization));
            LoginBean loginBean = loginService.getOne(Wrappers.<LoginBean>lambdaQuery().eq(LoginBean::getAdminId, value).select(LoginBean::getAdminInfo));
            String adminInfo = loginBean.getAdminInfo();
            AdminConsoleUserEntity entity = JSON.parseObject(adminInfo, AdminConsoleUserEntity.class);
            return ResponseFactory.success(entity);
        } catch (Exception e) {
            log.error("login q error:{}：", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
