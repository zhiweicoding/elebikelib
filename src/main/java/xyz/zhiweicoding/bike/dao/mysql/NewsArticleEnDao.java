package xyz.zhiweicoding.bike.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import xyz.zhiweicoding.bike.models.NewsArticleEnBean;

/**
 * 英文文章DAO
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 */
@Component
public interface NewsArticleEnDao extends BaseMapper<NewsArticleEnBean> {
}
