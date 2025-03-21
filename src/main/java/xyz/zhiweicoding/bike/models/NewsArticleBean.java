package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhiweicoding.xyz
 * @date 3/22/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("news_articles")
public class NewsArticleBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String articleId;
    private String title;
    private String photoUrl;
    private String author;
    private Date publishTime;
    private Integer hits;
    private String content;
    private Date createdAt;
    private String orgContent;
}