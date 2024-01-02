package xyz.zhiweicoding.bike.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEntity implements Serializable {
    private String openId;
    private String unionId;
    private String avatarUrl;
    private String city;
    private String country;
    private String gender;
    private String language;
    private String nickName;
    private String province;
}
