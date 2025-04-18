package xyz.zhiweicoding.bike.services;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
public interface GoodService extends IService<GoodBean> {
    IndexEntity getIndex();

    CatalogEntity getCatalog(CatalogVo param);

    GoodBean saveGoodBean(Map<String, Object> goodBean);
}
