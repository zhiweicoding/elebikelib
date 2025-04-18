package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.ConfigBean;
import xyz.zhiweicoding.bike.services.ConfigService;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.List;
import java.util.Map;

/**
 * banner
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/page/bpc")
public class PageBpcController {

    private static final Logger log = LoggerFactory.getLogger(PageGoodController.class);

    @Autowired
    @Qualifier(value = "configService")
    private ConfigService configService;

    /**
     * banner 分类页面查询
     * id
     * link
     * imageUrl
     *
     * @return
     */
    @PostMapping("/index")
    public BaseResponse<AntArrayEntity<Map<String, String>>> index(HttpServletRequest request, int current,
            int pageSize) {
        try {
            ConfigBean one = configService.getOne(Wrappers.<ConfigBean>lambdaQuery().eq(ConfigBean::getConfigId, "2")
                    .select(ConfigBean::getConfigContent));
            String configContent = one.getConfigContent();
            List<Map<String, String>> banners = JSON.parseObject(configContent, new TypeReference<>() {
            });
            AntArrayEntity<Map<String, String>> result = new AntArrayEntity<>(current, banners, pageSize,
                    banners.size());
            return ResponseFactory.success(result);
        } catch (Exception e) {
            log.error("首页banner查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * banner 保存
     * 
     * @param request
     * @param param
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<String> save(HttpServletRequest request, @RequestBody Map<String, Object> param) {
        try {
            ConfigBean one = configService.getOne(Wrappers.<ConfigBean>lambdaQuery().eq(ConfigBean::getConfigId, "2")
                    .select(ConfigBean::getConfigContent));
            String configContent = one.getConfigContent();
            List<Map<String, Object>> banners = JSON.parseObject(configContent, new TypeReference<>() {
            });
            if (param.containsKey("imageUrl")) {
                List<Map<String, Object>> obsArray = (List<Map<String, Object>>) param.get("imageUrl");
                if (!obsArray.isEmpty()) {
                    Map<String, Object> obs = obsArray.get(0);
                    param.put("imageUrl", obs.get("response"));
                }
            }
            banners.stream().map(b -> Integer.parseInt(String.valueOf(b.get("id")))).max(Integer::compareTo)
                    .ifPresent(max -> param.put("id", max + 1));
            banners.add(param);
            configService.update(Wrappers.<ConfigBean>lambdaUpdate()
                    .set(ConfigBean::getConfigContent, JSON.toJSONString(banners)).eq(ConfigBean::getConfigId, "2"));
            return ResponseFactory.success("success");
        } catch (Exception e) {
            log.error("首页banner查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * banner 更新
     * 
     * @param request
     * @param param
     * @return
     */
    @PutMapping("/update")
    public BaseResponse<String> update(HttpServletRequest request, @RequestBody Map<String, Object> param) {
        try {
            ConfigBean one = configService.getOne(Wrappers.<ConfigBean>lambdaQuery().eq(ConfigBean::getConfigId, "2")
                    .select(ConfigBean::getConfigContent));
            String configContent = one.getConfigContent();
            List<Map<String, Object>> banners = JSON.parseObject(configContent, new TypeReference<>() {
            });

            for (Map<String, Object> banner : banners) {
                String id = String.valueOf(banner.get("id"));
                if (id.equals(param.get("id"))) {
                    banner.putAll(param);
                    break;
                }
            }
            configService.update(Wrappers.<ConfigBean>lambdaUpdate()
                    .set(ConfigBean::getConfigContent, JSON.toJSONString(banners)).eq(ConfigBean::getConfigId, "2"));
            return ResponseFactory.success("success");
        } catch (Exception e) {
            log.error("首页banner查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

    /**
     * banner 删除
     * 
     * @param request
     * @param idArray
     * @return
     */
    @DeleteMapping("/removeList")
    public BaseResponse<String> removeList(HttpServletRequest request, @RequestBody List<String> idArray) {
        try {
            ConfigBean one = configService.getOne(Wrappers.<ConfigBean>lambdaQuery().eq(ConfigBean::getConfigId, "2")
                    .select(ConfigBean::getConfigContent));
            String configContent = one.getConfigContent();
            List<Map<String, String>> banners = JSON.parseObject(configContent, new TypeReference<>() {
            });
            banners.removeIf(banner -> idArray.contains(banner.get("id")));
            configService.update(Wrappers.<ConfigBean>lambdaUpdate()
                    .set(ConfigBean::getConfigContent, JSON.toJSONString(banners)).eq(ConfigBean::getConfigId, "2"));
            return ResponseFactory.success("success");
        } catch (Exception e) {
            log.error("分类页面查询 error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }

}
