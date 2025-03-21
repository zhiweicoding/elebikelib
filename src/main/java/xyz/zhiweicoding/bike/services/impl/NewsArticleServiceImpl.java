package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.NewsArticleDao;
import xyz.zhiweicoding.bike.models.NewsArticleBean;
import xyz.zhiweicoding.bike.services.NewsArticleService;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Service(value = "newsArticleService")
@Slf4j
public class NewsArticleServiceImpl extends ServiceImpl<NewsArticleDao, NewsArticleBean> implements NewsArticleService {
}