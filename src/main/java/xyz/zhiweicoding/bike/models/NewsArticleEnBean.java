package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 英文文章实体类（支持阿拉伯语翻译）
 * 
 * @author zhiweicoding.xyz
 * @date 11/11/25
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("news_articles_en")
public class NewsArticleEnBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String articleId;
    
    /**
     * 英文标题
     */
    private String title;
    
    private String photoUrl;
    
    private String author;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
    
    private Integer hits;
    
    /**
     * 英文简短内容
     */
    private String content;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    
    /**
     * 英文完整HTML内容
     */
    private String orgContent;
    
    /**
     * 阿拉伯语标题
     */
    private String titleAr;
    
    /**
     * 阿拉伯语简短内容
     */
    private String contentAr;
    
    /**
     * 阿拉伯语完整HTML内容
     */
    private String orgContentAr;
}
