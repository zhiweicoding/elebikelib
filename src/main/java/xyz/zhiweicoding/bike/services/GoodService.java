package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;
import xyz.zhiweicoding.bike.vo.api.IndexVo;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
public interface GoodService extends IService<GoodBean> {
    IndexEntity getIndex(IndexVo param);

    CatalogEntity getCatalog(CatalogVo param);
}
