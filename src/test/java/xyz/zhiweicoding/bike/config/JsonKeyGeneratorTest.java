package xyz.zhiweicoding.bike.config;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
class JsonKeyGeneratorTest {

    @Test
    void generate() {
        Object[] tests = new Object[3];
        tests[0] = 1111L;
        tests[1] = "abc";
        SearchVo searchVo = new SearchVo();
        searchVo.setKeyword("key");
        searchVo.setKeywordId("id");
        tests[2] = searchVo;
        String jsonString = JSON.toJSONString(tests);
        System.out.println(jsonString);
    }
}