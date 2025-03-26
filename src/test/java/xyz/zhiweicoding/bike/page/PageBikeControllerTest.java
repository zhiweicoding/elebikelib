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
import xyz.zhiweicoding.bike.models.BikeBean;
import xyz.zhiweicoding.bike.models.BikeImageBean;
import xyz.zhiweicoding.bike.services.BikeImageService;
import xyz.zhiweicoding.bike.services.BikeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PageBikeControllerTest {

    @Mock
    private BikeService bikeService;

    @Mock
    private BikeImageService bikeImageService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PageBikeController pageBikeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        // 准备测试数据
        int current = 1;
        int pageSize = 10;
        String title = "test bike";
        String category = "mountain";

        BikeBean bike = new BikeBean();
        bike.setProductId("test001");
        bike.setTitle(title);
        bike.setCategory(category);

        List<BikeBean> bikes = Arrays.asList(bike);
        Page<BikeBean> page = new Page<>(current, pageSize);
        page.setRecords(bikes);
        page.setTotal(1);

        // 模拟 BikeService 的行为
        when(bikeService.page(any(), any())).thenReturn(page);

        // 模拟 BikeImageService 的行为
        List<BikeImageBean> images = new ArrayList<>();
        BikeImageBean image = new BikeImageBean();
        image.setProductId("test001");
        image.setImagePath("/test.jpg");
        images.add(image);
        when(bikeImageService.list(any())).thenReturn(images);

        // 执行测试
        BaseResponse<AntArrayEntity<BikeBean>> response = pageBikeController.index(request, title, category, current,
                pageSize);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().getTotal());
        assertEquals(1, response.getData().getList().size());
        assertEquals(title, response.getData().getList().get(0).getTitle());
        assertNotNull(response.getData().getList().get(0).getImages());
    }

    @Test
    void testSave() {
        // 准备测试数据
        BikeBean bike = new BikeBean();
        bike.setProductId("test001");
        bike.setTitle("test bike");

        BikeImageBean image = new BikeImageBean();
        image.setImagePath("/test.jpg");
        List<BikeImageBean> images = Arrays.asList(image);
        bike.setImages(images);

        // 模拟服务行为
        when(bikeService.save(any())).thenReturn(true);
        when(bikeImageService.save(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageBikeController.save(request, bike);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("test001", response.getData());
        verify(bikeService, times(1)).save(any());
        verify(bikeImageService, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        BikeBean bike = new BikeBean();
        bike.setProductId("test001");
        bike.setTitle("updated bike");

        BikeImageBean image = new BikeImageBean();
        image.setImagePath("/new.jpg");
        List<BikeImageBean> images = Arrays.asList(image);
        bike.setImages(images);

        // 模拟服务行为
        when(bikeService.updateById(any())).thenReturn(true);
        when(bikeImageService.remove(any())).thenReturn(true);
        when(bikeImageService.save(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageBikeController.update(request, bike);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("test001", response.getData());
        verify(bikeService, times(1)).updateById(any());
        verify(bikeImageService, times(1)).remove(any());
        verify(bikeImageService, times(1)).save(any());
    }

    @Test
    void testRemoveList() {
        // 准备测试数据
        List<String> idArray = Arrays.asList("test001", "test002");

        // 模拟服务行为
        when(bikeService.removeByIds(any())).thenReturn(true);
        when(bikeImageService.remove(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageBikeController.removeList(request, idArray);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("test001,test002", response.getData());
        verify(bikeService, times(1)).removeByIds(idArray);
        verify(bikeImageService, times(1)).remove(any());
    }

    @Test
    void testSaveImage() {
        // 准备测试数据
        BikeImageBean image = new BikeImageBean();
        image.setProductId("test001");
        image.setImagePath("/test.jpg");

        // 模拟服务行为
        when(bikeImageService.save(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageBikeController.saveImage(request, image);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("test001", response.getData());
        verify(bikeImageService, times(1)).save(image);
    }

    @Test
    void testGetImages() {
        // 准备测试数据
        String productId = "test001";
        List<BikeImageBean> images = new ArrayList<>();
        BikeImageBean image = new BikeImageBean();
        image.setProductId(productId);
        image.setImagePath("/test.jpg");
        images.add(image);

        // 模拟服务行为
        when(bikeImageService.list(any())).thenReturn(images);

        // 执行测试
        BaseResponse<List<BikeImageBean>> response = pageBikeController.getImages(request, productId);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
        assertEquals(productId, response.getData().get(0).getProductId());
        verify(bikeImageService, times(1)).list(any());
    }
}