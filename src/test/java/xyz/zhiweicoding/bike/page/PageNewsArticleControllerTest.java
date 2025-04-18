package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zhiweicoding.bike.entity.AntArrayEntity;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.NewsArticleBean;
import xyz.zhiweicoding.bike.services.NewsArticleService;
import xyz.zhiweicoding.bike.support.ResponseFactory;
import xyz.zhiweicoding.bike.utils.GeneratorUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PageNewsArticleControllerTest {

    @Mock
    private NewsArticleService newsArticleService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PageNewsArticleController pageNewsArticleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        // 准备测试数据
        int current = 1;
        int pageSize = 10;
        String title = "Test News";
        String author = "John Doe";

        NewsArticleBean article = new NewsArticleBean();
        article.setArticleId("NEWS001");
        article.setTitle(title);
        article.setAuthor(author);
        article.setPublishTime(new Date());

        List<NewsArticleBean> articles = Arrays.asList(article);
        Page<NewsArticleBean> page = new Page<>(current, pageSize);
        page.setRecords(articles);
        page.setTotal(1);

        // 模拟服务行为
        when(newsArticleService.page(any(), any())).thenReturn(page);

        // 执行测试
        BaseResponse<AntArrayEntity<NewsArticleBean>> response = pageNewsArticleController.index(
                request, title, author, current, pageSize);

        // 验证结果
        assertNotNull(response);
        assertEquals(ResponseFactory.StatsEnum.SUCCESS.getCode(), response.getMsgCode());
        assertEquals(1, response.getMsgBody().getTotal());
        assertEquals(1, response.getMsgBody().getData().size());
        assertEquals(title, response.getMsgBody().getData().get(0).getTitle());
        assertEquals(author, response.getMsgBody().getData().get(0).getAuthor());
    }

    @Test
    void testSave() {
        // 准备测试数据
        String articleId = "NEWS001";
        NewsArticleBean article = new NewsArticleBean();
        article.setTitle("Test News");
        article.setAuthor("John Doe");
        article.setContent("Test Content");

        // 模拟 GeneratorUtil
        mockStatic(GeneratorUtil.class);
        when(GeneratorUtil.getCommonId()).thenReturn(articleId);

        // 模拟服务行为
        when(newsArticleService.save(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageNewsArticleController.save(request, article);

        // 验证结果
        assertNotNull(response);
        assertEquals(ResponseFactory.StatsEnum.SUCCESS.getCode(), response.getMsgCode());
        assertEquals(articleId, response.getMsgBody());
        verify(newsArticleService, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        NewsArticleBean article = new NewsArticleBean();
        article.setArticleId("NEWS001");
        article.setTitle("Updated News");
        article.setAuthor("John Doe");

        // 模拟服务行为
        when(newsArticleService.updateById(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageNewsArticleController.update(request, article);

        // 验证结果
        assertNotNull(response);
        assertEquals(ResponseFactory.StatsEnum.SUCCESS.getCode(), response.getMsgCode());
        assertEquals("NEWS001", response.getMsgBody());
        verify(newsArticleService, times(1)).updateById(any());
    }

    @Test
    void testRemoveList() {
        // 准备测试数据
        List<String> idArray = Arrays.asList("NEWS001", "NEWS002");

        // 模拟服务行为
        when(newsArticleService.removeByIds(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageNewsArticleController.removeList(request, idArray);

        // 验证结果
        assertNotNull(response);
        assertEquals(ResponseFactory.StatsEnum.SUCCESS.getCode(), response.getMsgCode());
        assertEquals("NEWS001,NEWS002", response.getMsgBody());
        verify(newsArticleService, times(1)).removeByIds(idArray);
    }
}