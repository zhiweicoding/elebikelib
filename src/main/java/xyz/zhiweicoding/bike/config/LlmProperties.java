package xyz.zhiweicoding.bike.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * LLM配置属性类
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "llm")
public class LlmProperties {
    
    /**
     * LLM提供商：glm
     */
    private String provider = "glm";
    
    /**
     * 智谱AI配置
     */
    private ZhipuConfig zhipu = new ZhipuConfig();
    
    /**
     * 超时时间（秒）
     */
    private Integer timeoutSeconds = 30;
    
    /**
     * 系统提示词
     */
    private String systemPrompt;
    
    /**
     * 保存/更新时是否自动翻译
     */
    private Boolean autoTranslateOnWrite = false;
    
    @Data
    public static class ZhipuConfig {
        /**
         * API Key
         */
        private String apiKey;
        
        /**
         * 模型名称
         */
        private String model = "glm-4-flash-250414";
    }
}
