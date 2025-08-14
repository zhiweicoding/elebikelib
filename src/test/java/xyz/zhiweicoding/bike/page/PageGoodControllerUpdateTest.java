package xyz.zhiweicoding.bike.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zhiweicoding.bike.entity.BaseResponse;
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.models.GoodBean;
import xyz.zhiweicoding.bike.services.BikeImageService;
import xyz.zhiweicoding.bike.services.BikeService;
import xyz.zhiweicoding.bike.services.GoodService;
import xyz.zhiweicoding.bike.support.CaffeineSupport;
import xyz.zhiweicoding.bike.support.ResponseFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 测试PageGoodController中bike和bikeimg的更新逻辑
 */
@SpringBootTest
class PageGoodControllerUpdateTest {

    @Mock
    private GoodService goodService;

    @Mock
    private BikeService bikeService;

    @Mock
    private BikeImageService bikeImageService;

    @Mock
    private CaffeineSupport caffeineSupport;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PageGoodController pageGoodController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateGoodWithBikeData() {
        // 准备测试数据
        GoodBean goodBean = new GoodBean();
        goodBean.setGoodId("g123456");
        goodBean.setGoodTitle("测试电动车");
        goodBean.setPcSymbolId("electric_bike");
        goodBean.setListPicUrl("http://example.com/image.jpg");
        goodBean.setPhotoUrl("[\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/523ea9d622f04e5eb6e133b162616456.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/2603144dfef549d7b2caaa9e6c817aa7.png\"]");

        // Mock服务调用
        when(goodService.updateById(any(GoodBean.class))).thenReturn(true);
        when(bikeService.update(any(), any(LambdaQueryWrapper.class))).thenReturn(true);
        when(bikeImageService.update(any(), any(LambdaQueryWrapper.class))).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageGoodController.update(request, goodBean);

        // 验证结果
        assertNotNull(response);
        assertEquals("g123456", response.getData());

        // 验证服务调用
        verify(goodService, times(1)).updateById(goodBean);
        verify(bikeService, times(1)).update(any(), any(LambdaQueryWrapper.class));
        verify(bikeImageService, times(1)).update(any(), any(LambdaQueryWrapper.class));
        verify(caffeineSupport, times(1)).removePattern("page:good:*");
    }

    @Test
    void testRemoveListWithBikeData() {
        // 准备测试数据
        List<String> idArray = Arrays.asList("g123456", "g789012");

        // Mock服务调用
        when(goodService.update(any(), any(LambdaQueryWrapper.class))).thenReturn(true);
        when(bikeService.remove(any(LambdaQueryWrapper.class))).thenReturn(true);
        when(bikeImageService.remove(any(LambdaQueryWrapper.class))).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageGoodController.removeList(request, idArray);

        // 验证结果
        assertNotNull(response);
        assertEquals("g123456,g789012", response.getData());

        // 验证服务调用
        verify(goodService, times(1)).update(any(), any(LambdaQueryWrapper.class));
        verify(bikeService, times(2)).remove(any(LambdaQueryWrapper.class)); // 每个ID调用一次
        verify(bikeImageService, times(2)).remove(any(LambdaQueryWrapper.class)); // 每个ID调用一次
        verify(caffeineSupport, times(1)).removePattern("page:good:*");
    }

    @Test
    void testUpdateWithException() {
        // 准备测试数据
        GoodBean goodBean = new GoodBean();
        goodBean.setGoodId("g123456");

        // Mock异常
        when(goodService.updateById(any(GoodBean.class))).thenThrow(new RuntimeException("Database error"));

        // 执行测试
        BaseResponse<String> response = pageGoodController.update(request, goodBean);

        // 验证结果
        assertNotNull(response);
        assertNull(response.getData());
        assertFalse(response.getSuccess());
    }

    @Test
    void testGenerateDetailHtmlWithValidPhotoUrl() {
        // 准备测试数据 - 模拟真实的photoUrl JSON数组
        String photoUrl = "[\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/523ea9d622f04e5eb6e133b162616456.png\",\"https://bike-1256485110.cos.ap-beijing.myqcloud.com/2603144dfef549d7b2caaa9e6c817aa7.png\"]";

        GoodBean goodBean = new GoodBean();
        goodBean.setGoodId("g123456");
        goodBean.setGoodTitle("测试电动车");
        goodBean.setPcSymbolId("electric_bike");
        goodBean.setListPicUrl("http://example.com/image.jpg");
        goodBean.setPhotoUrl(photoUrl);

        // Mock服务调用
        when(goodService.updateById(any(GoodBean.class))).thenReturn(true);
        when(bikeService.update(any(), any(LambdaQueryWrapper.class))).thenReturn(true);
        when(bikeImageService.update(any(), any(LambdaQueryWrapper.class))).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageGoodController.update(request, goodBean);

        // 验证结果
        assertNotNull(response);
        assertEquals("g123456", response.getData());
        assertTrue(response.getSuccess());

        // 验证服务调用
        verify(goodService, times(1)).updateById(goodBean);
        verify(bikeService, times(1)).update(any(), any(LambdaQueryWrapper.class));
        verify(bikeImageService, times(1)).update(any(), any(LambdaQueryWrapper.class));
    }
}
