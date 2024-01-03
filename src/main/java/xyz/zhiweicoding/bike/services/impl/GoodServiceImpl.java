package xyz.zhiweicoding.bike.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.zhiweicoding.bike.constants.ConfigFileConstant;
import xyz.zhiweicoding.bike.dao.mysql.ConfigDao;
import xyz.zhiweicoding.bike.dao.mysql.GoodDao;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.enums.CatalogEnum;
import xyz.zhiweicoding.bike.enums.FlagEnum;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;
import xyz.zhiweicoding.bike.vo.api.IndexVo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhiwei
 * @description 针对表【t_good】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "goodService")
@Slf4j
public class GoodServiceImpl extends ServiceImpl<GoodDao, GoodBean> implements GoodService {

    private final GoodDao goodDao;
    private final SymbolDao symbolDao;

    private final ConfigDao configDao;

    public GoodServiceImpl(GoodDao goodDao, SymbolDao symbolDao, ConfigDao configDao) {
        this.goodDao = goodDao;
        this.symbolDao = symbolDao;
        this.configDao = configDao;
    }

    @Override
    public IndexEntity getIndex(IndexVo param) {
        IndexEntity resultBean = new IndexEntity();
        List<GoodBean> goodAllList = goodDao.selectList(Wrappers.<GoodBean>lambdaQuery()
                .eq(GoodBean::getIsDelete, 0));
        List<SymbolBean> symbolAllLists = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery()
                .eq(SymbolBean::getIsDelete, 0));
        List<GoodBean> filterBeans = goodAllList.stream()
                .filter(bean -> bean.getIsChosen() == 1 && bean.getIsNew() == 0 && bean.getIsCheap() == 0)
                .collect(Collectors.toList());
        resultBean.setTopics(filterBeans);

        List<IndexEntity.FloorGood> floorGoods = new ArrayList<>();
        for (SymbolBean symbol : symbolAllLists) {
            String symbolId = symbol.getSymbolId();
            List<GoodBean> temps = goodAllList.stream().filter(b -> b.getSymbolId().equals(symbolId)).collect(Collectors.toList());
            symbol.setGoodBeans(temps);

            IndexEntity.FloorGood floorGood = new IndexEntity.FloorGood();
            floorGood.setId(symbol.getSymbolId());
            floorGood.setName(symbol.getSymbolName());
            List<GoodBean> firstPartTempList = temps.stream().limit(7).collect(Collectors.toList());
            floorGood.setGoodsList(firstPartTempList);
            floorGoods.add(floorGood);
        }
        resultBean.setFloorGoods(floorGoods);

        getBanner(resultBean);

        return resultBean;
    }

    /**
     * 获取首页轮播图的配置
     *
     * @param resultBean {@link IndexEntity}
     */
    private void getBanner(IndexEntity resultBean) {
        ConfigBean bannerConfig = null;
        try {
            bannerConfig = configDao.selectOne(Wrappers.<ConfigBean>lambdaQuery()
                    .eq(ConfigBean::getConfigType, ConfigFileConstant.HOME_BANNER_URL));
        } catch (Exception e) {
            log.error("check t_config home_banner_url config, duplicate config_type,{}", ConfigFileConstant.HOME_BANNER_URL, e);
        }
        if (bannerConfig != null && bannerConfig.getConfigContent() != null) {
            String configContent = bannerConfig.getConfigContent();
            List<IndexEntity.Banner> banners = JSON.parseObject(configContent, new TypeReference<>() {
            });
            resultBean.setBanners(banners);
        }
    }

    @Override
    public CatalogEntity getCatalog(CatalogVo param) {
        CatalogEntity sendBean = new CatalogEntity();

        LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery()
                .eq(GoodBean::getIsDelete, 0)
                .orderByDesc(GoodBean::getIsChosen)
                .orderByDesc(GoodBean::getIsNew)
                .orderByDesc(GoodBean::getIsCheap);
        String order = param.getOrder();
        String sort = param.getSort();
        String categoryId = param.getCategoryId();
        FlagEnum ascDesc = FlagEnum.getByName(order);
        if (Objects.requireNonNull(ascDesc) == FlagEnum.DESC) {
            wrapper.orderByDesc(GoodBean::getRetailPrice);
        } else {
            wrapper.orderByAsc(GoodBean::getRetailPrice);
        }
        CatalogEnum isNew = CatalogEnum.getByName(sort);
        //todo need to check
        if (Objects.requireNonNull(isNew) == CatalogEnum.DEFAULT) {
            wrapper.eq(GoodBean::getIsNew, 1);
        }

        List<SymbolBean> symbolAllList = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery().eq(SymbolBean::getIsDelete, 0));
        if (symbolAllList.isEmpty()) {
            log.warn("无分类，数据有误");
            return sendBean;
        }
        List<CatalogEntity.FilterCategory> filterCategories = new ArrayList<>();
        for (int i = 0; i < symbolAllList.size(); ++i) {
            SymbolBean bean = symbolAllList.get(i);
            CatalogEntity.FilterCategory filterCategory = new CatalogEntity.FilterCategory();
            filterCategory.setChecked(i == 0);
            filterCategory.setId(bean.getSymbolId());
            filterCategory.setName(bean.getSymbolName());
            filterCategories.add(filterCategory);
        }
        sendBean.setFilterCategories(filterCategories);

        if (!categoryId.equals("0")) {
            wrapper.eq(GoodBean::getSymbolId, categoryId);
            String symbolName = symbolAllList.get(0).getSymbolName();
            symbolName = symbolAllList.stream()
                    .filter(b -> b.getSymbolId().equals(categoryId))
                    .map(SymbolBean::getSymbolName)
                    .findFirst()
                    .orElse(symbolName);
            sendBean.setSymbolName(symbolName);
            CatalogEntity.FilterCategory filterCategory = new CatalogEntity.FilterCategory();
            filterCategory.setChecked(false);
            filterCategory.setId("0");
            filterCategory.setName("全部");
            filterCategories.add(0, filterCategory);
        } else {
            sendBean.setSymbolName("");
            filterCategories.get(0).setChecked(false);
            CatalogEntity.FilterCategory filterCategory = new CatalogEntity.FilterCategory();
            filterCategory.setChecked(true);
            filterCategory.setId("0");
            filterCategory.setName("全部");
            filterCategories.add(0, filterCategory);
        }

        List<GoodBean> goodBeans = goodDao.selectList(wrapper);
        goodBeans = goodBeans.stream().sorted((o1, o2) -> {
            Optional<SymbolBean> opt1 = symbolAllList.stream()
                    .filter(b -> b.getSymbolId().equals(o1.getSymbolId()))
                    .findFirst();
            Optional<SymbolBean> opt2 = symbolAllList.stream()
                    .filter(b -> b.getSymbolId().equals(o2.getSymbolId()))
                    .findFirst();
            if (opt1.isPresent() && opt2.isPresent()) {
                int sortNum1 = opt1.get().getSortNum();
                int sortNum2 = opt2.get().getSortNum();
                return Integer.compare(sortNum1, sortNum2);
            } else if (opt1.isEmpty()) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        sendBean.setGoodsList(goodBeans);

        sendBean.setCategoryFilter(false);
        return sendBean;
    }
}



