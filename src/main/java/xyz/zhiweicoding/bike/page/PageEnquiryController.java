package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.EnquiryBean;
import xyz.zhiweicoding.bike.services.EnquiryService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Date;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
@RestController
@RequestMapping(value = "/v1/page/enquiry")
@Slf4j
public class PageEnquiryController {

    @Autowired
    @Qualifier(value = "enquiryService")
    private EnquiryService enquiryService;

    /**
     * 询盘页面查询
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<EnquiryBean>> index(HttpServletRequest request,
                                                           String contact,
                                                           String telOrMail,
                                                           Integer status,
                                                           int current,
                                                           int pageSize) {
        try {
            Page<EnquiryBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<EnquiryBean> wrapper = Wrappers.<EnquiryBean>lambdaQuery();

            if (contact != null && !contact.isEmpty()) {
                wrapper.like(EnquiryBean::getContact, contact);
            }
            if (telOrMail != null && !telOrMail.isEmpty()) {
                wrapper.like(EnquiryBean::getTelOrMail, telOrMail);
            }
            if (status != null) {
                wrapper.eq(EnquiryBean::getStatus, status);
            }

            wrapper.orderByDesc(EnquiryBean::getCreateTime);

            Page<EnquiryBean> pageResult = enquiryService.page(page, wrapper);
            AntArrayEntity<EnquiryBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(),
                    pageResult.getRecords(), pageSize, (int) pageResult.getTotal());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("询盘页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 保存询盘
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody EnquiryBean enquiryBean) {
        try {
            enquiryBean.setCreateTime(new Date());
            enquiryBean.setStatus(0); // 默认未处理状态
            enquiryBean.setIp(getClientIp(request));
            enquiryService.save(enquiryBean);
            return ResponseFactory.success(String.valueOf(enquiryBean.getId()));
        } catch (Exception e) {
            log.error("保存询盘 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 更新询盘状态
     */
    @PutMapping("/updateStatus")
    public BaseResponse<String> updateStatus(HttpServletRequest request,
                                             @RequestParam Long id,
                                             @RequestParam Integer status) {
        try {
            EnquiryBean enquiry = new EnquiryBean();
            enquiry.setId(id);
            enquiry.setStatus(status);
            enquiryService.updateById(enquiry);
            return ResponseFactory.success(id.toString());
        } catch (Exception e) {
            log.error("更新询盘状态 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 批量删除询盘
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<Long> idArray) {
        try {
            enquiryService.removeByIds(idArray);
            return ResponseFactory.success(String.join(",", idArray.stream().map(String::valueOf).toList()));
        } catch (Exception e) {
            log.error("删除询盘 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}