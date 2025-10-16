package xyz.zhiweicoding.bike.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云配置类
 *
 * @author zhiwei
 * @date 2025/01/15
 */
@Configuration
public class QiniuConfig {

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    @Value("${qiniu.prefix:article/}")
    private String prefix;

    @Value("${qiniu.expire:7200}")
    private Long expire;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public String getDomain() {
        return domain;
    }

    public String getPrefix() {
        return prefix;
    }

    public Long getExpire() {
        return expire;
    }
}
