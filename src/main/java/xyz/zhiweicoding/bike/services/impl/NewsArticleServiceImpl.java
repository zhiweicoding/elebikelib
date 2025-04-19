package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.NewsArticleDao;
import xyz.zhiweicoding.bike.models.NewsArticleBean;
import xyz.zhiweicoding.bike.services.NewsArticleService;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Service(value = "newsArticleService")
public class NewsArticleServiceImpl extends ServiceImpl<NewsArticleDao, NewsArticleBean> implements NewsArticleService {

    private static final Logger log = LoggerFactory.getLogger(NewsArticleServiceImpl.class);

    @Override
    public void saveNewsArticle(NewsArticleBean newsArticleBean) {
        try {
            // 查询最大的article_id并加1
            NewsArticleBean maxArticle = this.getOne(
                    Wrappers.<NewsArticleBean>lambdaQuery()
                            .orderByDesc(NewsArticleBean::getArticleId)
                            .last("LIMIT 1"));

            Set<String> articleIds = this.list(Wrappers.<NewsArticleBean>lambdaQuery()
                    .select(NewsArticleBean::getArticleId))
                    .stream()
                    .map(NewsArticleBean::getArticleId)
                    .distinct()
                    .collect(Collectors.toSet());

            if (maxArticle != null && maxArticle.getArticleId() != null) {
                try {
                    long maxId = Long.parseLong(maxArticle.getArticleId());
                    newsArticleBean.setArticleId(String.valueOf(maxId + 1));
                    log.info("Generated new article ID: {}", newsArticleBean.getArticleId());
                } catch (NumberFormatException e) {
                    String randomId = String.valueOf(1 + (int) (Math.random() * 9))
                            + String.valueOf((int) (Math.random() * 900000) + 100000);
                    // 检查生成的ID是否已存在，如果存在则重新生成
                    while (articleIds.contains(randomId)) {
                        randomId = String.valueOf(1 + (int) (Math.random() * 9))
                                + String.valueOf((int) (Math.random() * 900000) + 100000);
                        log.info("ID已存在，重新生成随机ID: {}", randomId);
                    }
                    newsArticleBean.setArticleId(randomId);
                    log.info("Generated random 7-digit article ID: {}", randomId);
                    log.warn("Could not parse article ID as number: {}", maxArticle.getArticleId());
                }
            }
            newsArticleBean.setHits((int) (Math.random() * 999));
            newsArticleBean.setCreatedAt(new Date());
            String orgContent = newsArticleBean.getOrgContent();
            // 将原始内容格式化为规范的HTML结构
            if (orgContent != null && !orgContent.isEmpty()) {
                // 构建规范的HTML结构
                StringBuilder formattedContent = new StringBuilder();
                formattedContent.append("<section id=\"readMore\">\n");
                formattedContent.append("<article id=\"contentNewW").append(newsArticleBean.getArticleId())
                        .append("\">\n");
                // 在每个</p>后添加<br/>
                String contentWithBreaks = orgContent.replace("</p>", "</p><br/>");
                formattedContent.append(contentWithBreaks);
                formattedContent.append("\n</article>\n");
                formattedContent.append("</section>");

                // 更新内容
                newsArticleBean.setOrgContent(formattedContent.toString());
                log.info("Content formatted to standard HTML structure with line breaks");
            }

            this.save(newsArticleBean);
        } catch (Exception e) {
            log.error("保存新闻文章 error：" + e.getMessage(), e);
        }
    }

    @Override
    public void updateNewsArticle(NewsArticleBean newsArticleBean) {
        try {
            String orgContent = newsArticleBean.getOrgContent();
            // 将原始内容格式化为规范的HTML结构
            if (orgContent != null && !orgContent.isEmpty()) {
                // 构建规范的HTML结构
                StringBuilder formattedContent = new StringBuilder();
                formattedContent.append("<section id=\"readMore\">\n");
                formattedContent.append("<article id=\"contentNewW").append(newsArticleBean.getArticleId())
                        .append("\">\n");
                // 在每个</p>后添加<br/>
                String contentWithBreaks = orgContent.replace("</p>", "</p><br/>");
                formattedContent.append(contentWithBreaks);
                formattedContent.append("\n</article>\n");
                formattedContent.append("</section>");

                // 更新内容
                newsArticleBean.setOrgContent(formattedContent.toString());
                log.info("Content formatted to standard HTML structure with line breaks for update");
            }

            UpdateWrapper<NewsArticleBean> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", newsArticleBean.getId());
            updateWrapper.set("photo_url", newsArticleBean.getPhotoUrl()); // 即使为null也会包含在更新语句中

            this.update(newsArticleBean, updateWrapper);
        } catch (Exception e) {
            log.error("更新新闻文章 error：" + e.getMessage(), e);
        }
    }
}