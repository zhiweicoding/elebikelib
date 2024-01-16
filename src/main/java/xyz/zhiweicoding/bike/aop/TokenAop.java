package xyz.zhiweicoding.bike.aop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.LoginBean;
import xyz.zhiweicoding.bike.services.LoginService;
import xyz.zhiweicoding.bike.support.RedisSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Map;

/**
 * aop除了登录登出接口外其他接口，检测header中是否有Authorization的key，检测redis中是否存在该key，如果存在则放行，否则返回错误信息
 *
 * @author zhiweicoding.xyz
 * @date 1/12/24
 * @email diaozhiwei2k@gmail.com
 */
@Aspect
@Slf4j
@Component
public class TokenAop {

    @Pointcut("execution(public * xyz.zhiweicoding.bike.api.*.*(..)) ")
    public void apiPointCut() {
    }

    @Pointcut("execution(public * xyz.zhiweicoding.bike.page.*.*(..)) && !execution(* xyz.zhiweicoding.bike.page.PageLoginController.*(..))")
    public void pagePointCut() {
    }


    @SuppressWarnings("all")
    @Autowired
    private RedisSupport redisSupport;

    @Autowired
    @Qualifier(value = "loginService")
    private LoginService loginService;

    @Around("pagePointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                    break;
                }
            }

            if (request != null) {
                String requestURI = request.getRequestURI();
                log.debug("请求地址：{}", requestURI);
                String remoteHost = request.getRemoteHost();
                log.debug("请求ip:{}", remoteHost);
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (String key : parameterMap.keySet()) {
                    log.debug("参数：{}入参：{}", key, parameterMap.get(key));
                }
                //检测header中是否有Authorization的key，检测redis中是否存在该key，如果存在则放行，否则返回错误信息
                String authorization = request.getHeader("Authorization");
                if (redisSupport.exists(authorization)) {
                    String value = String.valueOf(redisSupport.get(authorization));
                    boolean exists = loginService.exists(Wrappers.<LoginBean>lambdaQuery()
                            .eq(LoginBean::getAdminId, value)
                            .eq(LoginBean::getIsDelete, 0)
                            .eq(LoginBean::getAdminRole, 1));
                    if (exists) {
                        res = joinPoint.proceed();
                        time = System.currentTimeMillis() - time;
                        log.debug("方法一共运行了：{}ms", time);
                        return res;
                    } else {
                        return ResponseFactory.noToken("请登录后重试");
                    }
                } else {
                    return ResponseFactory.noToken("请登录后重试");
                }
            } else {
                return ResponseFactory.get(ResponseFactory.StatsEnum.ERROR_URL, "错误的地址");
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return ResponseFactory.fail("系统异常，请稍后重试");
        }
    }

}
