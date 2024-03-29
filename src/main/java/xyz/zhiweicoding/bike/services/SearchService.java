package xyz.zhiweicoding.bike.services;

import xyz.zhiweicoding.bike.entity.EsQueryResponse;

import java.util.List;

/**
 * @author zhiwei
 * @description 针对表【t_content(内容表)】的数据库操作Service
 * @createDate 2022-03-20 15:41:26
 */
public interface SearchService {


    /**
     * 小程序内容简单查询
     *
     * @param msg
     * @return
     */
    List<EsQueryResponse.SourceBean> searchSimple(int fId, String msg);

    /**
     * 根据bid查询
     * @param fId
     * @param msg
     * @return
     */
    List<EsQueryResponse.SourceBean> searchSimpleById(int fId, String msg);

    /**
     * 小程序内容复杂查询
     *
     * @param title
     * @param msg
     * @param author
     * @param start
     * @param end
     * @return
     */
    List<EsQueryResponse.SourceBean> searchComplex(int fId, String title, String msg, String author, int start, int end);

    /**
     * 根据bid查询
     * @param fId
     * @param title
     * @param msg
     * @param author
     * @param start
     * @param end
     * @return
     */
    List<EsQueryResponse.SourceBean> searchComplexById(int fId, String title, String msg, String author, int start, int end);

}
