package xyz.zhiweicoding.bike.services;

import com.alibaba.fastjson2.JSON;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试save和update方法生成的HTML是否一致
 */
public class SaveVsUpdateHtmlTest {
    
    public static void main(String[] args) {
        // 模拟photoUrl数据
        List<String> photoUrlList = List.of(
            "https://bike-1256485110.cos.ap-beijing.myqcloud.com/523ea9d622f04e5eb6e133b162616456.png",
            "https://bike-1256485110.cos.ap-beijing.myqcloud.com/2603144dfef549d7b2caaa9e6c817aa7.png"
        );
        
        String photoUrlJson = JSON.toJSONString(photoUrlList);
        
        System.out.println("测试数据:");
        System.out.println("photoUrlList: " + photoUrlList);
        System.out.println("photoUrlJson: " + photoUrlJson);
        System.out.println();
        
        // 模拟save方法的HTML生成逻辑
        String saveHtml = generateSaveHtml(photoUrlList);
        System.out.println("Save方法生成的HTML:");
        System.out.println(saveHtml);
        System.out.println();
        
        // 模拟update方法的HTML生成逻辑
        String updateHtml = generateUpdateHtml(photoUrlJson);
        System.out.println("Update方法生成的HTML:");
        System.out.println(updateHtml);
        System.out.println();
        
        // 比较结果
        boolean isEqual = saveHtml.equals(updateHtml);
        System.out.println("两种方法生成的HTML是否一致: " + isEqual);
        
        if (!isEqual) {
            System.out.println("\n差异分析:");
            System.out.println("Save HTML 长度: " + saveHtml.length());
            System.out.println("Update HTML 长度: " + updateHtml.length());
        }
    }
    
    /**
     * 模拟save方法中的HTML生成逻辑 (修改后)
     */
    private static String generateSaveHtml(List<String> photoUrlList) {
        StringBuilder detailHtmlBuilder = new StringBuilder();
        for (String photoUrl : photoUrlList) {
            detailHtmlBuilder.append("<p style=\"text-align: center;\"><img data-src=\"")
                    .append(photoUrl)
                    .append("\" src=\"https://bodocn-1256485110.cos.ap-beijing.myqcloud.com/images/imgbg.png\" style=\"\"/></p>")
                    .append("<p><br/></p>");
        }
        return detailHtmlBuilder.toString();
    }
    
    /**
     * 模拟update方法中的HTML生成逻辑
     */
    private static String generateUpdateHtml(String photoUrl) {
        if (photoUrl == null || photoUrl.trim().isEmpty()) {
            return "<p><br/></p>";
        }
        
        try {
            // 解析JSON数组
            List<String> photoUrlList = JSON.parseArray(photoUrl, String.class);
            if (photoUrlList == null || photoUrlList.isEmpty()) {
                return "<p><br/></p>";
            }
            
            StringBuilder detailHtmlBuilder = new StringBuilder();
            
            // 为每个图片URL生成HTML段落
            for (String imageUrl : photoUrlList) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    detailHtmlBuilder.append("<p style=\"text-align: center;\">")
                            .append("<img data-src=\"").append(imageUrl).append("\" ")
                            .append("src=\"https://bodocn-1256485110.cos.ap-beijing.myqcloud.com/images/imgbg.png\" ")
                            .append("style=\"\"/>")
                            .append("</p>")
                            .append("<p><br/></p>");
                }
            }
            
            return detailHtmlBuilder.toString();
        } catch (Exception e) {
            System.err.println("Failed to generate detail HTML from photoUrl: " + photoUrl);
            e.printStackTrace();
            return "<p><br/></p>";
        }
    }
}
