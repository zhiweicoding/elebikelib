package xyz.zhiweicoding.bike.api;

import xyz.zhiweicoding.bike.entity.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 模板api
 * @Created by zhiwei on 2022/3/11.
 */
@RestController
@RequestMapping(value = "/v1/api/authFile")
@Slf4j
public class AuthFileController {

    @RequestMapping(value = "/get/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    BaseResponse<String> login(@PathVariable String id) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setMsgCode(1000);
        response.setMsgBody(id);
        response.setMsgInfo("msg");
        return response;
    }


}
