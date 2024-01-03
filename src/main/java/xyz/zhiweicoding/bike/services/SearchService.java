package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.entity.api.SearchEntity;
import xyz.zhiweicoding.bike.entity.api.SearchRedirectEntity;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;
import xyz.zhiweicoding.bike.vo.api.IndexVo;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
public interface SearchService {

    SearchEntity getSearch(SearchVo param);

    List<SearchEntity.KeywordBean> getKeywordList(SearchVo param);

    BaseResponse<SearchRedirectEntity> getRedirect(SearchVo param);
}
