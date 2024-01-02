package xyz.zhiweicoding.bike.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ControllerInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.debug("requestURI-----"+requestURI);
        if (!requestURI.contains("/login")
                && !requestURI.contains("/resources/")
                && !requestURI.contains("/static/")
                && !requestURI.contains("/v1/api/")) {
            return false;
        }

        return true;
    }

    /**
     * freemaker base字段拦截注入
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("base", request.getContextPath());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        response.addHeader("Content-Security-Policy", "upgrade-insecure-requests");
    }

}
