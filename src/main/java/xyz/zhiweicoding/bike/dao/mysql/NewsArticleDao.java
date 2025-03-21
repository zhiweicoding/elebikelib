package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.NewsArticleBean;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Component
public interface NewsArticleDao extends BaseMapper<NewsArticleBean> {
}