package xyz.zhiweicoding.bike.services;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zhiweicoding.bike.models.ImgBean;

import java.util.List;
import java.util.Map;

/**
 * @author zhiwei
 * @description 针对表【t_img(图片库)】的数据库操作Service
 * @createDate 2022-03-20 15:41:26
 */
public interface ImgService extends IService<ImgBean> {

    /**
     * 小程序彩图首次获取图片
     *
     * @param fId
     * @param currentPage
     * @return
     */
    Map<String, Object> getMiniImgs(int fId, int currentPage);

    Map<String, Object> getImgKg(int fId);

    /**
     * 根据 book id获取load more
     *
     * @param bId
     * @param currentPage
     * @return
     */
    Map<String, Object> byBId(int bId, int currentPage);

    Map<String, Object> byBIdKg(int bId);

    /**
     * 小程序img简单查询
     *
     * @param msg
     * @return
     */
    List<ImgBean> searchSimple(int fId, String msg);

    /**
     * 小程序img复杂查询
     *
     * @param title
     * @param msg
     * @param author
     * @param start
     * @param end
     * @return
     */
    List<ImgBean> searchComplex(int fId, String title, String msg, String author, int start, int end);

}
