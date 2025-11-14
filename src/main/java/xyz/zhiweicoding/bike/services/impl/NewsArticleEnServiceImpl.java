package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.NewsArticleEnDao;
import xyz.zhiweicoding.bike.models.NewsArticleEnBean;
import xyz.zhiweicoding.bike.services.NewsArticleEnService;
import xyz.zhiweicoding.bike.support.ArabicTranslationSupport;

import java.util.*;

/**
 * 英文文章Service实现
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
@Slf4j
@Service("newsArticleEnService")
public class NewsArticleEnServiceImpl extends ServiceImpl<NewsArticleEnDao, NewsArticleEnBean> 
        implements NewsArticleEnService {

    @Autowired(required = false)
    private ArabicTranslationSupport translationSupport;

    @Override
    public void saveNewsArticleEn(NewsArticleEnBean article) {
        try {
            // 生成article_id（逻辑同中文版本）
            NewsArticleEnBean maxArticle = this.getOne(
                    Wrappers.<NewsArticleEnBean>lambdaQuery()
                            .orderByDesc(NewsArticleEnBean::getArticleId)
                            .last("LIMIT 1"));

            if (maxArticle != null && maxArticle.getArticleId() != null) {
                try {
                    long maxId = Long.parseLong(maxArticle.getArticleId());
                    article.setArticleId(String.valueOf(maxId + 1));
                } catch (NumberFormatException e) {
                    String randomId = String.valueOf(1 + (int) (Math.random() * 9))
                            + String.valueOf((int) (Math.random() * 900000) + 100000);
                    article.setArticleId(randomId);
                }
            }
            
            article.setHits((int) (Math.random() * 999));
            article.setCreatedAt(new Date());
            
            // 格式化HTML内容
            if (article.getOrgContent() != null && !article.getOrgContent().isEmpty()) {
                StringBuilder formattedContent = new StringBuilder();
                formattedContent.append("<section id=\"readMore\">\n");
                formattedContent.append("<article id=\"contentNewW").append(article.getArticleId()).append("\">\n");
                String contentWithBreaks = article.getOrgContent().replace("</p>", "</p><br/>");
                formattedContent.append(contentWithBreaks);
                formattedContent.append("\n</article>\n</section>");
                article.setOrgContent(formattedContent.toString());
            }

            this.save(article);
            log.info("保存英文文章成功, articleId={}", article.getArticleId());
        } catch (Exception e) {
            log.error("保存英文文章失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    @Override
    public void updateNewsArticleEn(NewsArticleEnBean article) {
        try {
            // 格式化HTML内容
            if (article.getOrgContent() != null && !article.getOrgContent().isEmpty()) {
                StringBuilder formattedContent = new StringBuilder();
                formattedContent.append("<section id=\"readMore\">\n");
                formattedContent.append("<article id=\"contentNewW").append(article.getArticleId()).append("\">\n");
                String contentWithBreaks = article.getOrgContent().replace("</p>", "</p><br/>");
                formattedContent.append(contentWithBreaks);
                formattedContent.append("\n</article>\n</section>");
                article.setOrgContent(formattedContent.toString());
            }

            UpdateWrapper<NewsArticleEnBean> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", article.getId());
            updateWrapper.set("photo_url", article.getPhotoUrl());

            this.update(article, updateWrapper);
            log.info("更新英文文章成功, id={}", article.getId());
        } catch (Exception e) {
            log.error("更新英文文章失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> translateArticleById(Integer id, boolean overwriteExisting) {
        if (translationSupport == null) {
            throw new RuntimeException("翻译服务未配置，请设置 llm.provider=glm");
        }

        NewsArticleEnBean article = this.getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在: id=" + id);
        }

        // 检查是否已有翻译
        if (!overwriteExisting && article.getTitleAr() != null && !article.getTitleAr().isEmpty()) {
            log.info("文章已有阿拉伯语翻译且不覆盖, id={}", id);
            Map<String, String> existing = new HashMap<>();
            existing.put("title_ar", article.getTitleAr());
            existing.put("org_content_ar", article.getOrgContentAr());
            existing.put("content_ar", article.getContentAr());
            return existing;
        }

        // 调用翻译服务
        Map<String, String> translated = translationSupport.translateToArabic(
                article.getTitle(), 
                article.getOrgContent()
        );

        // 更新数据库
        article.setTitleAr(translated.get("title_ar"));
        article.setOrgContentAr(translated.get("org_content_ar"));
        article.setContentAr(translated.get("content_ar"));
        this.updateById(article);

        log.info("文章翻译成功, id={}", id);
        return translated;
    }

    @Override
    public Map<String, Object> translateArticlesByIds(List<Integer> ids, boolean overwriteExisting) {
        List<Integer> successIds = new ArrayList<>();
        List<Map<String, Object>> failedList = new ArrayList<>();

        for (Integer id : ids) {
            try {
                translateArticleById(id, overwriteExisting);
                successIds.add(id);
            } catch (Exception e) {
                log.error("翻译文章失败, id={}", id, e);
                Map<String, Object> failInfo = new HashMap<>();
                failInfo.put("id", id);
                failInfo.put("error", e.getMessage());
                failedList.add(failInfo);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", successIds);
        result.put("failed", failedList);
        result.put("successCount", successIds.size());
        result.put("failedCount", failedList.size());
        return result;
    }
}
