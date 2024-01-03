package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

/**
 * 目录页
 *
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/catalog")
@Slf4j
public class CatalogController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 获取目录页的信息
     * 15 min cache
     *
     * @param param {@link CatalogVo}
     * @return
     */
    @Cacheable(value = "30m", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/query")
    public BaseResponse<CatalogEntity> query(@RequestBody CatalogVo param) {
        log.debug("获取目录页的信息,入参 : {}", JSON.toJSONString(param));
        try {
            log.debug("获取目录页的信息 success");
            SearchRequest searchRequest = new SearchRequest("query_s0*");
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : search.getHits().getHits()) {
                log.info(hit.getSourceAsString());
            }
            CatalogEntity catalog = goodService.getCatalog(param);
            BaseResponse<CatalogEntity> success = ResponseFactory.success(catalog);
            success.setMsgBodySize(1);
            return success;
        } catch (Exception e) {
            log.error("获取目录页的信息 error:{}：", e.getMessage(), e);
            BaseResponse<CatalogEntity> fail = ResponseFactory.fail(null);
            fail.setMsgBodySize(0);
            return fail;
        }
    }


}
