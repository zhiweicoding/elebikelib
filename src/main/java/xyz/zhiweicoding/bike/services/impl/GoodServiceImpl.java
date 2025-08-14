package xyz.zhiweicoding.bike.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.constants.ConfigFileConstant;
import xyz.zhiweicoding.bike.dao.mysql.BikeDao;
import xyz.zhiweicoding.bike.dao.mysql.BikeImageDao;
import xyz.zhiweicoding.bike.dao.mysql.ConfigDao;
import xyz.zhiweicoding.bike.dao.mysql.GoodDao;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.api.CatalogEntity;
import xyz.zhiweicoding.bike.entity.api.IndexEntity;
import xyz.zhiweicoding.bike.enums.FlagEnum;
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;
import xyz.zhiweicoding.bike.vo.api.CatalogVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhiwei
 * @description 针对表【t_good】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "goodService")
public class GoodServiceImpl extends ServiceImpl<GoodDao, GoodBean> implements GoodService {

    private static final Logger log = LoggerFactory.getLogger(GoodServiceImpl.class);

    @Autowired
    private SymbolDao symbolDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private BikeDao bikeDao;

    @Autowired
    private BikeImageDao bikeImageDao;

    @Override
    public IndexEntity getIndex() {
        IndexEntity resultBean = new IndexEntity();
        List<GoodBean> goodAllList = baseMapper.selectList(Wrappers.<GoodBean>lambdaQuery()
                .eq(GoodBean::getIsDelete, 0));
        List<SymbolBean> symbolAllLists = symbolDao.selectList(Wrappers.<SymbolBean>lambdaQuery()
                .eq(SymbolBean::getIsDelete, 0));
        List<String> popularKeyArray = symbolAllLists.stream().filter(b -> b.getIsPopular() == 1)
                .sorted(Comparator.comparingInt(SymbolBean::getSortNum)).map(SymbolBean::getSymbolId).toList();
        List<GoodBean> popularGoodArray = goodAllList.stream().filter(b -> popularKeyArray.contains(b.getSymbolId()))
                .limit(20).collect(Collectors.toList());
        resultBean.setTopics(!popularGoodArray.isEmpty() ? popularGoodArray : new ArrayList<>());

        List<IndexEntity.FloorGood> floorGoods = new ArrayList<>();
        for (SymbolBean symbol : symbolAllLists) {
            String symbolId = symbol.getSymbolId();
            List<GoodBean> temps = goodAllList.stream().filter(b -> b.getSymbolId().equals(symbolId))
                    .collect(Collectors.toList());
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
            log.error("check t_config home_banner_url config, duplicate config_type,{}",
                    ConfigFileConstant.HOME_BANNER_URL, e);
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
        String symbolId = param.getSymbolId();// symbol id
        int place = param.getPlace();// 基地
        int isChosen = param.getIsChosen();// 推荐
        int isNew = param.getIsNew();// 基地
        int isPopular = param.getIsPopular();// 爆款
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

        List<GoodBean> goodBeans = baseMapper.selectList(wrapper);
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

    @Override
    public GoodBean saveGoodBean(Map<String, Object> goodBean) {
        String generatorIdString = GeneratorUtil.getCommonId();
        GoodBean good = new GoodBean();
        good.setGoodTitle(String.valueOf(goodBean.get("goodTitle")));
        good.setGoodBrief(String.valueOf(goodBean.get("goodBrief")));
        good.setSymbolId(String.valueOf(goodBean.get("symbolId")));
        good.setPcSymbolId(String.valueOf(goodBean.get("pcSymbolId")));
        good.setRetailPrice(Double.parseDouble(String.valueOf(goodBean.get("retailPrice"))));
        good.setIsNew(Integer.parseInt(String.valueOf(goodBean.get("isNew"))));
        good.setIsChosen(Integer.parseInt(String.valueOf(goodBean.get("isChosen"))));
        good.setScenePicUrl(getUrl(goodBean, "scenePicUrl"));
        good.setListPicUrl(getUrl(goodBean, "listPicUrl"));
        List<Map<String, Object>> photoUrlArray = (List<Map<String, Object>>) goodBean.get("photoUrl");
        List<String> photoUrlList = new ArrayList<>();
        for (Map<String, Object> item : photoUrlArray) {
            photoUrlList.add(String.valueOf(item.get("response")));
        }
        good.setPhotoUrl(JSON.toJSONString(photoUrlList));
        good.setIsDelete(0);
        good.setCreateTime(System.currentTimeMillis());
        good.setModifyTime(System.currentTimeMillis());
        good.setGoodId("g" + generatorIdString);
        good.setGoodNumber(9999999);
        good.setLikeNum(new Random().nextInt(999999));
        this.save(good);

        List<BikeBean> bikesWithoutPrev = bikeDao.selectList(
                Wrappers.<BikeBean>lambdaQuery()
                        .and(wrapper -> wrapper.eq(BikeBean::getPrevProductId, "")
                                .or()
                                .isNull(BikeBean::getPrevProductId)));

        // 构建detail_html，将photo_url中的图片URL转换为HTML格式
        StringBuilder detailHtmlBuilder = new StringBuilder();
        for (String photoUrl : photoUrlList) {
            detailHtmlBuilder.append("<p style=\"text-align: center;\"><img data-src=\"")
                    .append(photoUrl)
                    .append("\" src=\"https://bodocn-1256485110.cos.ap-beijing.myqcloud.com/images/imgbg.png\" style=\"\"/></p>")
                    .append("<p><br/></p>");
        }

        // 保存bikes记录，新记录的prev_product_id和prev_product_title设为空
        BikeBean bike = new BikeBean();
        bike.setProductId("p" + generatorIdString);
        bike.setTitle(good.getGoodTitle());
        bike.setCategory(String.valueOf(goodBean.get("pcSymbolId")));
        bike.setDetailHtml(detailHtmlBuilder.toString());
        bike.setPrevProductId(""); // 新记录的前置记录ID设为空
        bike.setPrevProductTitle(""); // 新记录的前置记录标题设为空
        bike.setCreatedAt(new Date());
        bike.setUpdatedAt(new Date());
        bikeDao.insert(bike);

        // 保存bike_images记录，将list_pic_url作为主图
        BikeImageBean bikeImage = new BikeImageBean();
        bikeImage.setProductId("i" + generatorIdString);
        bikeImage.setImagePath(good.getListPicUrl());
        bikeImage.setIsMain(1); // 设置为主图
        bikeImageDao.insert(bikeImage);

        // 更新这些记录，使用当前记录作为它们的前置记录
        if (!bikesWithoutPrev.isEmpty()) {
            BikeBean bikeBean = bikesWithoutPrev.get(0);
            bikeDao.update(Wrappers.<BikeBean>lambdaUpdate()
                    .set(BikeBean::getPrevProductId, "p" + generatorIdString)
                    .set(BikeBean::getPrevProductTitle, good.getGoodTitle())
                    .eq(BikeBean::getProductId, bikeBean.getProductId()));
        }

        return good;
    }

    private String getUrl(Map<String, Object> bean, String key) {
        String url = "";
        try {
            List<Map<String, Object>> array = (List<Map<String, Object>>) bean.get(key);
            url = String.valueOf(array.get(0).get("response"));
        } catch (Exception e) {
            log.error(" error：" + e.getMessage(), e);
        }
        return url;
    }

}
