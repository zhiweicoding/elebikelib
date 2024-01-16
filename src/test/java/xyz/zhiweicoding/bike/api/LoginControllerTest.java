package xyz.zhiweicoding.bike.api;

import cn.hutool.crypto.digest.MD5;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 1/11/24
 * @email diaozhiwei2k@gmail.com
 */
class LoginControllerTest {

    @Test
    void in() {
        System.out.println(MD5.create().digestHex("baodao.<123>"));
    }
}