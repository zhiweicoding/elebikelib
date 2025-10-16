package xyz.zhiweicoding.bike.services;

import java.util.Map;

/**
 * 七牛云服务接口
 *
 * @author zhiwei
 * @date 2025/01/15
 */
public interface QiniuService {

    /**
     * 生成七牛云上传凭证
     *
     * @return Map包含token和domain
     */
    Map<String, String> generateUploadToken();
}
