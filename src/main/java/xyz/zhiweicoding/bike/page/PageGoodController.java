package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.List;

/**
 * 商品页
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


    @PostMapping("/index")
    public BaseResponse<Page<GoodBean>> index(HttpServletRequest request, String anyText, int current, int size, double priceMin, double priceMax, int isNew, int isCheap, int isChosen) {
        try {
            Page<GoodBean> page = new Page<>(current, size);
            LambdaQueryWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaQuery().eq(GoodBean::getIsDelete, 0);
            if (!anyText.isEmpty()) {
                wrapper.and(andWrapper -> andWrapper.like(GoodBean::getGoodTitle, anyText)
                        .or()
                        .like(GoodBean::getGoodBrief, anyText)
                        .or()
                        .like(GoodBean::getTagList, anyText));
            }
            if (priceMin > 0) {
                wrapper.ge(GoodBean::getRetailPrice, priceMin);
            }
            if (priceMax > 0) {
                wrapper.le(GoodBean::getRetailPrice, priceMax);
            }
            if (isNew != -1) {
                wrapper.eq(GoodBean::getIsNew, isNew);
            }
            if (isCheap != -1) {
                wrapper.eq(GoodBean::getIsCheap, isCheap);
            }
            if (isChosen != -1) {
                wrapper.eq(GoodBean::getIsChosen, isChosen);
            }
            Page<GoodBean> pageResult = goodService.page(page, wrapper);
            BaseResponse<Page<GoodBean>> resp = ResponseFactory.success(pageResult);
            resp.setMsgBodySize((int) pageResult.getPages());
            return resp;
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody GoodBean goodBean) {
        try {
            goodBean.setGoodId(GeneratorUtil.getCommonId());
            goodService.save(goodBean);
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("save good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody GoodBean goodBean) {
        try {
            goodService.updateById(goodBean);
            return ResponseFactory.success(goodBean.getGoodId());
        } catch (Exception e) {
            log.error("update good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/remove")
    public BaseResponse<String> remove(HttpServletRequest request, @RequestParam String id) {
        try {
            goodService.update(null, Wrappers.<GoodBean>lambdaUpdate()
                    .set(GoodBean::getIsDelete, -1)
                    .eq(GoodBean::getGoodId, id));
            return ResponseFactory.success(id);
        } catch (Exception e) {
            log.error("remove good error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    @PostMapping("/removeList")
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
