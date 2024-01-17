package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * page商品页
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/page/good")
@Slf4j
public class PageGoodController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;
    @Autowired
    @Qualifier(value = "symbolService")
    private SymbolService symbolService;


    /**
     * good page index query
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<GoodBean>> index(HttpServletRequest request, String symbolId,
                                                        String goodTitle, String goodBrief,
                                                        String isNew, String isChosen,
                                                        int current, int pageSize) {
        try {
            Page<GoodBean> page = new Page<>(current, pageSize);
            LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0);
            if (goodTitle != null && !goodTitle.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodTitle, goodTitle));
            }
            if (goodBrief != null && !goodBrief.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodBrief, goodBrief));
            }
            if (isNew != null && !isNew.isEmpty()) {
                wrapper.eq(GoodBean::getIsNew, isNew);
            }
            if (isChosen != null && !isChosen.isEmpty()) {
                wrapper.eq(GoodBean::getIsChosen, isChosen);
            }
            if (symbolId != null && !symbolId.isEmpty()) {
                wrapper.eq(GoodBean::getSymbolId, symbolId);
            }
            Page<GoodBean> pageResult = goodService.page(page, wrapper);
            AntArrayEntity<GoodBean> result = new AntArrayEntity<>((int) pageResult.getCurrent(), pageResult.getRecords(), pageSize, (int) pageResult.getTotal());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody Map<String, Object> goodBean) {
        try {
            GoodBean good = new GoodBean();
            good.setGoodTitle(String.valueOf(goodBean.get("goodTitle")));
            good.setGoodBrief(String.valueOf(goodBean.get("goodBrief")));
            good.setSymbolId(String.valueOf(goodBean.get("symbolId")));
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
            good.setGoodId(GeneratorUtil.getCommonId());
            good.setGoodNumber(9999999);
            good.setLikeNum(new Random().nextInt(999999));
            goodService.save(good);
            return ResponseFactory.success(good.getGoodId());
        } catch (Exception e) {
            log.error("save good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
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

    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody GoodBean goodBean) {
        try {
            goodService.updateById(goodBean);
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("update good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            goodService.update(null, Wrappers.<GoodBean>lambdaUpdate()
                    .set(GoodBean::getIsDelete, -1)
                    .in(GoodBean::getGoodId, idArray));
            return ResponseFactory.success(String.join(",", idArray));
        } catch (Exception e) {
            log.error("remove good batch error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
