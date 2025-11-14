package xyz.zhiweicoding.bike.support;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.config.LlmProperties;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿拉伯语翻译支持类
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "llm.provider", havingValue = "glm")
public class ArabicTranslationSupport {

    @Autowired
    private LlmProperties llmProperties;

    private ZhipuAiClient zhipuAiClient;

    @PostConstruct
    public void init() {
        String apiKey = llmProperties.getZhipu().getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("智谱AI API Key未配置，阿拉伯语翻译功能将不可用");
            return;
        }
        this.zhipuAiClient = ZhipuAiClient.builder()
                .apiKey(apiKey)
                .build();
        log.info("智谱AI客户端初始化成功, model={}", llmProperties.getZhipu().getModel());
    }

    /**
     * 翻译英文文章为阿拉伯语
     * 
     * @param title 英文标题
     * @param orgContent 英文HTML内容
     * @return 包含 title_ar, org_content_ar, content_ar 的Map
     */
    public Map<String, String> translateToArabic(String title, String orgContent) {
        if (zhipuAiClient == null) {
            throw new RuntimeException("智谱AI客户端未初始化");
        }

        long start = System.currentTimeMillis();
        log.info("开始翻译文章, 标题长度={}, 内容长度={}", title.length(), orgContent.length());

        try {
            // 构建用户提示词
            String userPrompt = buildUserPrompt(title, orgContent);
            
            // 调用LLM
            String response = callLLM(userPrompt);
            
            // 解析响应
            Map<String, String> result = parseResponse(response, title, orgContent);
            
            double elapsed = (System.currentTimeMillis() - start) / 1000.0;
            log.info("翻译完成, 耗时={}s, 标题长度={}, 内容长度={}, 摘要长度={}", 
                    String.format("%.1f", elapsed), 
                    result.get("title_ar").length(),
                    result.get("org_content_ar").length(),
                    result.get("content_ar").length());
            
            return result;
            
        } catch (Exception e) {
            log.error("翻译失败", e);
            throw new RuntimeException("翻译失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建用户提示词
     */
    private String buildUserPrompt(String title, String orgContent) {
        return String.format(
            "Please translate the following English article to Arabic:\n\n" +
            "Title: %s\n\n" +
            "HTML Content:\n%s\n\n" +
            "Requirements:\n" +
            "1. Translate title to Arabic (title_ar)\n" +
            "2. Translate HTML content to Arabic, preserving ALL HTML tags (org_content_ar)\n" +
            "3. Generate a 2-3 sentence Arabic summary without HTML tags (content_ar, 200-280 chars)\n" +
            "4. Return as JSON with keys: title_ar, org_content_ar, content_ar",
            title, orgContent
        );
    }

    /**
     * 调用LLM
     */
    private String callLLM(String userPrompt) {
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model(llmProperties.getZhipu().getModel())
                .temperature(0.1F)
                .messages(Arrays.asList(
                        ChatMessage.builder()
                                .role(ChatMessageRole.SYSTEM.value())
                                .content(llmProperties.getSystemPrompt())
                                .build(),
                        ChatMessage.builder()
                                .role(ChatMessageRole.USER.value())
                                .content(userPrompt)
                                .build()
                ))
                .build();

        ChatCompletionResponse response = zhipuAiClient.chat().createChatCompletion(request);
        
        if (!response.isSuccess()) {
            throw new RuntimeException("智谱AI调用失败: " + response.getMsg());
        }

        Object contentObj = response.getData().getChoices().get(0).getMessage().getContent();
        return contentObj != null ? contentObj.toString() : "";
    }

    /**
     * 解析LLM响应
     */
    private Map<String, String> parseResponse(String response, String originalTitle, String originalContent) {
        // 清理响应（移除markdown代码块标记）
        String cleaned = cleanResponse(response);
        
        try {
            // 尝试解析JSON
            JSONObject json = JSON.parseObject(cleaned);
            
            Map<String, String> result = new HashMap<>();
            result.put("title_ar", json.getString("title_ar"));
            result.put("org_content_ar", json.getString("org_content_ar"));
            result.put("content_ar", json.getString("content_ar"));
            
            // 如果content_ar为空，从org_content_ar提取摘要
            if (result.get("content_ar") == null || result.get("content_ar").isEmpty()) {
                result.put("content_ar", extractSummary(result.get("org_content_ar")));
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("JSON解析失败，尝试降级策略", e);
            // 降级策略：返回原文
            Map<String, String> fallback = new HashMap<>();
            fallback.put("title_ar", originalTitle);
            fallback.put("org_content_ar", originalContent);
            fallback.put("content_ar", extractSummary(originalContent));
            return fallback;
        }
    }

    /**
     * 清理响应内容
     */
    private String cleanResponse(String response) {
        String result = response.trim();
        
        // 移除markdown代码块
        if (result.startsWith("```json")) {
            result = result.substring(7);
        } else if (result.startsWith("```")) {
            result = result.substring(3);
        }
        
        if (result.endsWith("```")) {
            result = result.substring(0, result.length() - 3);
        }
        
        return result.trim();
    }

    /**
     * 从HTML内容提取纯文本摘要
     */
    private String extractSummary(String htmlContent) {
        try {
            String text = Jsoup.parse(htmlContent).text();
            // 限制为前280个字符
            return text.length() > 280 ? text.substring(0, 280) + "..." : text;
        } catch (Exception e) {
            log.error("提取摘要失败", e);
            return "";
        }
    }
}
