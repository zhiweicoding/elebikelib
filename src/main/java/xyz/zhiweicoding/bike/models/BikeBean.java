package xyz.zhiweicoding.bike.models;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName("bikes")
public class BikeBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String productId;
    private String title;
    private String category;
    private String detailHtml;
    private String prevProductId;
    private String prevProductTitle;
    private Date createdAt;
    private Date updatedAt;

    @TableField(exist = false)
    private List<BikeImageBean> images;

    /**
     * @return String return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return String return the detailHtml
     */
    public String getDetailHtml() {
        return detailHtml;
    }

    /**
     * @param detailHtml the detailHtml to set
     */
    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    /**
     * @return String return the prevProductId
     */
    public String getPrevProductId() {
        return prevProductId;
    }

    /**
     * @param prevProductId the prevProductId to set
     */
    public void setPrevProductId(String prevProductId) {
        this.prevProductId = prevProductId;
    }

    /**
     * @return String return the prevProductTitle
     */
    public String getPrevProductTitle() {
        return prevProductTitle;
    }

    /**
     * @param prevProductTitle the prevProductTitle to set
     */
    public void setPrevProductTitle(String prevProductTitle) {
        this.prevProductTitle = prevProductTitle;
    }

    /**
     * @return Date return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return Date return the updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return List<BikeImageBean> return the images
     */
    public List<BikeImageBean> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<BikeImageBean> images) {
        this.images = images;
    }

}