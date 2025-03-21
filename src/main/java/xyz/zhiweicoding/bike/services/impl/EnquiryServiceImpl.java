package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.EnquiryDao;
import xyz.zhiweicoding.bike.models.EnquiryBean;
import xyz.zhiweicoding.bike.services.EnquiryService;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Service(value = "enquiryService")
@Slf4j
public class EnquiryServiceImpl extends ServiceImpl<EnquiryDao, EnquiryBean> implements EnquiryService {
}