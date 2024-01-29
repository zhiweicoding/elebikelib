package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.zhiweicoding.bike.dao.mysql.ConfigDao;
import xyz.zhiweicoding.bike.dao.mysql.GoodDao;
import xyz.zhiweicoding.bike.dao.mysql.SymbolDao;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.entity.api.SearchEntity;
import xyz.zhiweicoding.bike.entity.api.SearchRedirectEntity;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.models.SymbolBean;
import xyz.zhiweicoding.bike.services.SearchService;
import xyz.zhiweicoding.bike.services.SymbolService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.vo.api.SearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhiwei
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "symbolService")
@Slf4j
public class SymbolServiceImpl extends ServiceImpl<SymbolDao, SymbolBean> implements SymbolService {

}




