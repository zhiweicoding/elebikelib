package xyz.zhiweicoding.bike.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.services.QiniuService;

import java.util.Map;

/**
 * 七牛云控制器
 *
 * @author zhiwei
 * @date 2025/01/15
 */
@RestController
@RequestMapping("/v1/api/qiniu")
public class PageQiniuController {

    private static final Logger log = LoggerFactory.getLogger(PageQiniuController.class);

    @Autowired
    private QiniuService qiniuService;

    /**
     * 获取七牛云上传凭证
     *
     * @return 包含token和domain的响应
     */
    @GetMapping("/token")
    public BaseResponse<Map<String, String>> getUploadToken() {
        try {
            log.info("请求获取七牛云上传凭证");
            Map<String, String> result = qiniuService.generateUploadToken();
            log.info("成功返回七牛云上传凭证");
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("获取七牛云上传凭证失败: {}", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
