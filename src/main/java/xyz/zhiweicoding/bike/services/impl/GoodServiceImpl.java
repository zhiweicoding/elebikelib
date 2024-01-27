package xyz.zhiweicoding.bike.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.constants.ConfigFileConstant;
import xyz.zhiweicoding.bike.dao.mysql.ConfigDao;
import xyz.zhiweicoding.bike.dao.mysql.GoodDao;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.enums.FlagEnum;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

import java.util.*;
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
    public IndexEntity getIndex() {
        IndexEntity resultBean = new IndexEntity();
        List<GoodBean> goodAllList = goodDao.selectList(Wrappers.<GoodBean>lambdaQuery()
                .eq(GoodBean::getIsDelete, 0));
        List<SymbolBean> symbolAllLists = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery()
                .eq(SymbolBean::getIsDelete, 0));
        List<String> popularKeyArray = symbolAllLists.stream().filter(b -> b.getIsPopular() == 1).sorted(Comparator.comparingInt(SymbolBean::getSortNum)).map(SymbolBean::getSymbolId).toList();
        List<GoodBean> popularGoodArray = goodAllList.stream().filter(b -> popularKeyArray.contains(b.getSymbolId())).limit(20).collect(Collectors.toList());
        resultBean.setTopics(!popularGoodArray.isEmpty() ? popularGoodArray : new ArrayList<>());

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
        sendBean.setValueFromVo(param);

        LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery()
                .eq(GoodBean::getIsDelete, 0)
                .orderByDesc(GoodBean::getIsChosen)
                .orderByDesc(GoodBean::getIsNew);
        String order = param.getOrder();// order by modifyTime
        String symbolId = param.getSymbolId();//symbol id
        int place = param.getPlace();//基地
        int isChosen = param.getIsChosen();//推荐
        int isNew = param.getIsNew();//基地
        int isPopular = param.getIsPopular();//爆款
        FlagEnum ascDesc = FlagEnum.getByName(order);
        if (Objects.requireNonNull(ascDesc) == FlagEnum.DESC) {
            wrapper.orderByDesc(GoodBean::getModifyTime);
        } else {
            wrapper.orderByAsc(GoodBean::getModifyTime);
        }
        if (isNew != -1) {
            wrapper.eq(GoodBean::getIsNew, isNew);
        }
        if (isChosen != -1) {
            wrapper.eq(GoodBean::getIsChosen, isChosen);
        }

        LambdaQueryWrapper<SymbolBean> symbolWrapper = Wrappers.<SymbolBean>lambdaQuery()
                .eq(SymbolBean::getIsDelete, 0);
        if (place != -1) {
            symbolWrapper.eq(SymbolBean::getPlace, place);
        }
        if (isPopular != -1) {
            symbolWrapper.eq(SymbolBean::getIsPopular, isPopular);
        }
        if (symbolId != null && !symbolId.isEmpty() && !symbolId.equals("-1")) {
            symbolWrapper.eq(SymbolBean::getSymbolId, symbolId);
            wrapper.eq(GoodBean::getSymbolId, symbolId);
        } else {
            sendBean.setSymbolName("全部");
        }
        if (param.getSearchValue() != null && !param.getSearchValue().isEmpty()) {
            wrapper.and(andWrapper -> {
                andWrapper
                        .like(GoodBean::getGoodTitle, param.getSearchValue())
                        .or()
                        .like(GoodBean::getGoodBrief, param.getSearchValue());
            });
        }
        List<SymbolBean> symbolAllList = symbolDao.selectList(symbolWrapper);
        if (symbolAllList.isEmpty()) {
            log.warn("无分类，数据有误");
            return sendBean;
        } else {
            sendBean.setSymbolName(symbolAllList.get(0).getSymbolName());
        }

        List<GoodBean> goodBeans = goodDao.selectList(wrapper);
        sortBySymbol(goodBeans, symbolAllList, sendBean);

        return sendBean;
    }

    /**
     * 使用symbol的 is_popular和sort_num进行排序
     *
     * @param goodBeans     从数据库中查到的数据
     * @param symbolAllList symbol的列表
     * @param sendBean      返回的bean
     */
    private static void sortBySymbol(List<GoodBean> goodBeans, List<SymbolBean> symbolAllList, CatalogEntity sendBean) {
        if (symbolAllList.size() > 1 && goodBeans.size() > 1) {
            goodBeans = goodBeans.stream()
                    .peek(b -> {
                        int weight = 0;
                        weight += b.getIsChosen();
                        weight += b.getIsNew();
                        Optional<SymbolBean> opt = symbolAllList.stream()
                                .filter(s -> s.getIsDelete() == 0 && s.getSymbolId().equals(b.getSymbolId()))
                                .findFirst();
                        if (opt.isPresent()) {
                            SymbolBean symbolBean = opt.get();
                            b.setSymbolName(symbolBean.getSymbolName());
                            weight += symbolBean.getIsPopular();
                            weight += (99999 - symbolBean.getSortNum());
                        }
                        b.setWeight(weight);
                    })
                    .sorted((o1, o2) -> o2.getWeight() - o1.getWeight())
                    .collect(Collectors.toList());
        }
        sendBean.setGoodsList(goodBeans);
    }
}




