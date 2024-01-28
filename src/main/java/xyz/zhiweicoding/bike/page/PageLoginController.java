package xyz.zhiweicoding.bike.page;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.LoginBean;
import xyz.zhiweicoding.bike.services.LoginService;
import xyz.zhiweicoding.bike.support.RedisSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * login
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/page/login")
@Slf4j
public class PageLoginController {

    @SuppressWarnings("all")
    @Autowired
    @Qualifier(value = "loginService")
    private LoginService loginService;

    @SuppressWarnings("all")
    @Autowired
    private RedisSupport redisSupport;

    /**
     * login in
     */
    @PostMapping("/in")
    public BaseResponse<String> in(@RequestBody Map<String, String> param) {
        log.debug("login in,入参 : {}", JSON.toJSONString(param));
        try {
            String username = param.get("username");
            String password = param.get("password");
            long timestamp = Long.parseLong(param.get("timestamp"));
            LambdaQueryWrapper<LoginBean> queries = Wrappers.<LoginBean>lambdaQuery()
                    .eq(LoginBean::getUsername, username)
                    .eq(LoginBean::getPassword, password)
                    .eq(LoginBean::getIsDelete, 0)
                    .eq(LoginBean::getAdminRole, 1);
            long count = loginService.count(queries);
            if (count == 1) {
                List<LoginBean> list = loginService.list(queries);
                for (LoginBean loginBean : list) {
                    String adminId = loginBean.getAdminId();
                    loginService.update(
                            Wrappers.<LoginBean>lambdaUpdate()
                                    .set(LoginBean::getModifyTime, System.currentTimeMillis())
                                    .eq(LoginBean::getAdminId, adminId)
                    );
                    String strMd5 = "Bearer " + MD5.create().digestHex(username + password + timestamp);
                    redisSupport.set(strMd5, adminId, 60 * 60 * 24 * 7L, TimeUnit.SECONDS);
                }
                return ResponseFactory.success("登录成功");
            } else {
                return ResponseFactory.get(ResponseFactory.StatsEnum.I_DO_NOT_KNOW, "账号或密码错误，请稍后重试");
            }
        } catch (Exception e) {
            log.error("login in error:{}：", e.getMessage(), e);
            return ResponseFactory.fail("登录失败");
        }
    }

    /**
     * login out
     */
    @PostMapping("/out")
    public BaseResponse<String> out(@RequestHeader("Authorization") String authorization) {
        log.debug("login out,入参 authorization : {}", authorization);
        try {
            if (redisSupport.exists(authorization)) {
                redisSupport.remove(authorization);
                return ResponseFactory.success("退出成功");
            } else {
                return ResponseFactory.noToken("无此账户信息");
            }
        } catch (Exception e) {
            log.error("login out error:{}：", e.getMessage(), e);
            return ResponseFactory.fail("登录失败");
        }
    }
}
