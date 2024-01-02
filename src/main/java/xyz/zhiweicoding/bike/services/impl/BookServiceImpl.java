package xyz.zhiweicoding.bike.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zhiweicoding.bike.dao.mysql.BookDao;
import xyz.zhiweicoding.bike.models.BookBean;
import xyz.zhiweicoding.bike.services.BookService;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_book(书籍表)】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookDao, BookBean> implements BookService {
}




