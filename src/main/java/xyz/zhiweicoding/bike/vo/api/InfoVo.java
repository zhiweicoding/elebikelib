package xyz.zhiweicoding.bike.vo.api;

import cn.hutool.system.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoVo implements Serializable {

    private String code;
    private UserInfo userInfo;
    private String appId;
}
