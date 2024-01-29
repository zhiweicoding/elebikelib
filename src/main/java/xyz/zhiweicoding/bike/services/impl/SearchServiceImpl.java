package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.SearchEntity;
import xyz.zhiweicoding.bike.entity.api.SearchRedirectEntity;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.SearchService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiwei
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "searchService")
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SymbolDao symbolDao;

    @Override
    public SearchEntity getSearch() {
        SearchEntity searchSendBean = new SearchEntity();
        List<SymbolBean> symbolAllList = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery().eq(SymbolBean::getIsDelete, 0));
        if (!symbolAllList.isEmpty()) {
            SearchEntity.DefaultKeyword defaultKeyword = new SearchEntity.DefaultKeyword();
            String symbolName = symbolAllList.get(0).getSymbolName();
            symbolName = symbolAllList.stream().filter((bean) -> bean.getIsPopular() == 1).map(SymbolBean::getSymbolName).findFirst().orElse(symbolName);
            defaultKeyword.setKeyword(symbolName);
            searchSendBean.setDefaultKeyword(defaultKeyword);
        }
        List<SearchEntity.KeywordBean> hotKeywords = symbolAllList.stream().map(b -> {
            SearchEntity.KeywordBean keywordBean = new SearchEntity.KeywordBean();
            keywordBean.setIsHot(b.getIsPopular());
            keywordBean.setKeyword(b.getSymbolName());
            keywordBean.setKeywordId(b.getSymbolId());
            return keywordBean;
        }).collect(Collectors.toList());
        searchSendBean.setHotKeywords(hotKeywords);

        return searchSendBean;
    }

    @Override
    public List<SearchEntity.KeywordBean> getKeywordList(SearchVo param) {
        if (param.getKeyword().isEmpty()) {
            return new ArrayList<>();
        } else {
            List<SymbolBean> symbolBeans = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery()
                    .like(SymbolBean::getSymbolName, param.getKeyword())
                    .eq(SymbolBean::getIsDelete, 0));
            return symbolBeans.stream().map(b -> {
                SearchEntity.KeywordBean keywordBean = new SearchEntity.KeywordBean();
                keywordBean.setIsHot(b.getIsPopular());
                keywordBean.setKeyword(b.getSymbolName());
                keywordBean.setKeywordId(b.getSymbolId());
                return keywordBean;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public BaseResponse<SearchRedirectEntity> getRedirect(SearchVo param) {
        BaseResponse<SearchRedirectEntity> sendBean = new BaseResponse<>();
        String keywordId = param.getKeywordId();
        SymbolBean symbolBean = new SymbolBean();
        symbolBean.setSymbolName(param.getKeyword());
        List<SymbolBean> symbolBeans;
        SearchRedirectEntity searchRedirectSendBean = new SearchRedirectEntity();
        if (keywordId == null) {
            symbolBeans = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery()
                    .like(SymbolBean::getSymbolName, param.getKeyword())
                    .eq(SymbolBean::getIsDelete, 0));
            if (!symbolBeans.isEmpty()) {
                SymbolBean firstOneSymbol = symbolBeans.get(0);
                searchRedirectSendBean.setKeywordId(firstOneSymbol.getSymbolId());
                searchRedirectSendBean.setPageUrl("../catalog/catalog");
                sendBean.setMsgBody(searchRedirectSendBean);
                sendBean = ResponseFactory.success(searchRedirectSendBean);
                sendBean.setMsgBodySize(1);
            } else {
                sendBean = ResponseFactory.get(ResponseFactory.StatsEnum.I_DO_NOT_KNOW, null);
                sendBean.setMsgBodySize(1);
            }
        } else {
            symbolBean.setSymbolId(keywordId);
            SymbolBean beanById = symbolDao.selectOne(Wrappers.<SymbolBean>lambdaQuery().eq(SymbolBean::getSymbolId, keywordId));
            if (beanById != null && !beanById.getSymbolId().isEmpty()) {
                searchRedirectSendBean.setKeywordId(beanById.getSymbolId());
                searchRedirectSendBean.setPageUrl("../catalog/catalog");
                sendBean = ResponseFactory.success(searchRedirectSendBean);
                sendBean.setMsgBodySize(1);
            } else {
                sendBean = ResponseFactory.get(ResponseFactory.StatsEnum.I_DO_NOT_KNOW, null);
                sendBean.setMsgBodySize(1);
            }
        }

        return sendBean;
    }

}




