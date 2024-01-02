package xyz.zhiweicoding.bike.entity.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.zhiweicoding.bike.entity.UserInfoEntity;

import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoEntity implements Serializable {

    private String openid;
    private String session_key;
    private String unionid;
    private String expires_in;
    private UserInfoEntity userInfo;
    private String contentToken;
}
