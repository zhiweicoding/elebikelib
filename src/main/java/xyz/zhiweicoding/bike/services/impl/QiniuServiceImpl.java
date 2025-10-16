package xyz.zhiweicoding.bike.services.impl;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.config.QiniuConfig;
import xyz.zhiweicoding.bike.services.QiniuService;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛云服务实现类
 *
 * @author zhiwei
 * @date 2025/01/15
 */
@Service
public class QiniuServiceImpl implements QiniuService {

    private static final Logger log = LoggerFactory.getLogger(QiniuServiceImpl.class);

    @Autowired
    private QiniuConfig qiniuConfig;

    @Override
    public Map<String, String> generateUploadToken() {
        try {
            log.info("开始生成七牛云上传凭证");

            // 创建认证对象
            Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());

            // 构建上传策略
            StringMap putPolicy = new StringMap();
            // 限制文件大小为 5MB
            putPolicy.put("fsizeLimit", 50 * 1024 * 1024);
            // 限制文件类型为图片
            putPolicy.put("mimeLimit", "image/*");

            // 生成上传凭证
            String uploadToken = auth.uploadToken(
                    qiniuConfig.getBucket(),
                    null,
                    qiniuConfig.getExpire(),
                    putPolicy
            );

            // 构建返回结果
            Map<String, String> result = new HashMap<>();
            result.put("token", uploadToken);
            result.put("domain", qiniuConfig.getDomain());

            log.info("七牛云上传凭证生成成功，domain: {}", qiniuConfig.getDomain());
            return result;

        } catch (Exception e) {
            log.error("生成七牛云上传凭证失败", e);
            throw new RuntimeException("生成上传凭证失败: " + e.getMessage(), e);
        }
    }
}
