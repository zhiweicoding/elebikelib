package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.CosUtil;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;
import xyz.zhiweicoding.bike.vo.api.IndexVo;

import java.util.UUID;

/**
 * 电动车分类接口
 *
 * @Created by zhiwei on 2024/1/1.
 */
@RestController
@RequestMapping(value = "/v1/api/upload")
@Slf4j
public class UploadController {

    @Autowired
    private CosUtil cosUtil;

    @Value("${cos.baseUrl}")
    private String cosBaseUrl;

    /**
     * upload common
     * https://bike-1256485110.cos.ap-beijing.myqcloud.com/
     */
    @PostMapping("/common")
    public String common(HttpServletRequest request, @RequestParam("files") MultipartFile files) {
        try {
            String originalFilename = files.getOriginalFilename();
            String commonId = GeneratorUtil.getCommonId();
            if (originalFilename != null && !originalFilename.isEmpty() && originalFilename.contains(".") && originalFilename.lastIndexOf(".") != originalFilename.length() - 1) {
                String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
                commonId = commonId + "." + substring;
            }
            cosUtil.upFile(commonId, files.getInputStream());
            return cosBaseUrl + commonId;
        } catch (Exception e) {
            return "";
        }
    }


}
