package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zhiweicoding.bike.dao.mysql.ImgDao;
import xyz.zhiweicoding.bike.models.ImgBean;
import xyz.zhiweicoding.bike.services.ImgService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhiwei
 * @description 针对表【t_img(图片库)】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service
public class ImgServiceImpl extends ServiceImpl<ImgDao, ImgBean> implements ImgService {

    /**
     * 小程序彩图首次获取图片
     *
     * @param fId
     * @param currentPage
     * @return
     */
    @Override
    public Map<String, Object> getMiniImgs(int fId, int currentPage) {
        return null;
    }

    @Override
    public Map<String, Object> getImgKg(int fId) {
        return null;
    }

    /**
     * 根据 book id获取load more
     *
     * @param bId
     * @param currentPage
     * @return
     */
    @Override
    public Map<String, Object> byBId(int bId, int currentPage) {
        return null;
    }

    @Override
    public Map<String, Object> byBIdKg(int bId) {
        return null;
    }

    /**
     * 小程序img简单查询
     *
     * @param fId
     * @param msg
     * @return
     */
    @Override
    public List<ImgBean> searchSimple(int fId, String msg) {
        return null;
    }

    /**
     * 小程序img复杂查询
     *
     * @param fId
     * @param title
     * @param msg
     * @param author
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<ImgBean> searchComplex(int fId, String title, String msg, String author, int start, int end) {
        return null;
    }
}




