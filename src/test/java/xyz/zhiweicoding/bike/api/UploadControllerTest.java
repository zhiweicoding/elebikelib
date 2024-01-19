package xyz.zhiweicoding.bike.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zhiweicoding.bike.dao.mysql.GoodDao;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.utils.CosUtil;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 1/19/24
 * @email diaozhiwei2k@gmail.com
 */
@SpringBootTest
class UploadControllerTest {

    @Autowired
    private CosUtil cosUtil;

    @Value("${cos.baseUrl}")
    private String cosBaseUrl;
    @Autowired
    private GoodDao goodDao;

    @Test
    void common() throws IOException {
        List<GoodBean> goodBeans = goodDao.selectList(Wrappers.lambdaQuery());
        for (GoodBean goodBean : goodBeans) {
            LambdaUpdateWrapper<GoodBean> wrapper = Wrappers.<GoodBean>lambdaUpdate()
                    .eq(GoodBean::getGoodId, goodBean.getGoodId());
            String scenePicUrl = goodBean.getScenePicUrl();
            int toUp = 0;
            if (scenePicUrl.contains("image.bodocn.com")) {
                String scenePicUrlMod = help(scenePicUrl);
                wrapper.set(GoodBean::getScenePicUrl, scenePicUrlMod);
                toUp++;
            }
            String listPicUrl = goodBean.getListPicUrl();
            if (listPicUrl.contains("image.bodocn.com")) {
                String listPicUrlMod = help(listPicUrl);
                wrapper.set(GoodBean::getListPicUrl, listPicUrlMod);
                toUp++;
            }
            String photoUrl = goodBean.getPhotoUrl();
            if (photoUrl.contains("image.bodocn.com")) {
                List<String> stringsMod = new ArrayList<>();
                List<String> strings = JSON.parseArray(photoUrl, String.class);
                for (String string : strings) {
                    if (string.contains("image.bodocn.com")) {
                        String stringMod = help(string);
                        stringsMod.add(stringMod);
                    }
                }
                wrapper.set(GoodBean::getPhotoUrl, JSON.toJSONString(stringsMod));
                toUp++;
            }
            if (toUp > 0) {
                goodDao.update(wrapper);
            }
        }
    }

    private String help(String fileStr) throws IOException {
        String fileName = GeneratorUtil.getCommonId() + ".png";
        String destinationFile = "/Users/zhiwei/Desktop/pcCache/temp/" + fileName;
        saveImage(fileStr, destinationFile);
        cosUtil.upFile(fileName, new File(destinationFile));
        return cosBaseUrl + fileName;
    }

    private static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }
}