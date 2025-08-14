package xyz.zhiweicoding.bike.page;

import com.alibaba.fastjson2.JSON;
import java.util.List;

/**
 * 演示detail_html生成逻辑
 */
public class DetailHtmlGeneratorDemo {
    
    public static void main(String[] args) {
        // 模拟t_good表中的photoUrl数据
        String photoUrl = "[\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/523ea9d622f04e5eb6e133b162616456.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/2603144dfef549d7b2caaa9e6c817aa7.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/10cf44739a934b95865f8bdd51df74a4.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/6b4184a54629487a988168b0c4ef4c86.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/127f89a7715944a39c153fbd0f1c6131.png\"]";
        
        System.out.println("原始photoUrl数据:");
        System.out.println(photoUrl);
        System.out.println();
        
        String detailHtml = generateDetailHtml(photoUrl);
        
        System.out.println("生成的detail_html:");
        System.out.println(detailHtml);
        System.out.println();
        
        System.out.println("格式化后的HTML:");
        System.out.println(formatHtml(detailHtml));
    }
    
    /**
     * 生成detail_html内容
     * 将photoUrl数组转换为HTML格式
     * 
     * @param photoUrl JSON格式的图片URL数组
     * @return HTML格式的详情内容
     */
    private static String generateDetailHtml(String photoUrl) {
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
    
    /**
     * 格式化HTML以便阅读
     */
    private static String formatHtml(String html) {
        return html.replace("><", ">\n<")
                  .replace("<p", "\n<p")
                  .replace("</p>", "</p>\n");
    }
}
