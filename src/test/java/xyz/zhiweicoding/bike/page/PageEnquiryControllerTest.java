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
import xyz.zhiweicoding.bike.models.EnquiryBean;
import xyz.zhiweicoding.bike.services.EnquiryService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PageEnquiryControllerTest {

    @Mock
    private EnquiryService enquiryService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PageEnquiryController pageEnquiryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        // 准备测试数据
        int current = 1;
        int pageSize = 10;
        String contact = "John";
        String telOrMail = "john@example.com";
        Integer status = 0;

        EnquiryBean enquiry = new EnquiryBean();
        enquiry.setId(1L);
        enquiry.setContact(contact);
        enquiry.setTelOrMail(telOrMail);
        enquiry.setStatus(status);
        enquiry.setCreateTime(new Date());

        List<EnquiryBean> enquiries = Arrays.asList(enquiry);
        Page<EnquiryBean> page = new Page<>(current, pageSize);
        page.setRecords(enquiries);
        page.setTotal(1);

        // 模拟服务行为
        when(enquiryService.page(any(), any())).thenReturn(page);

        // 执行测试
        BaseResponse<AntArrayEntity<EnquiryBean>> response = pageEnquiryController.index(
                request, contact, telOrMail, status, current, pageSize);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().getTotal());
        assertEquals(1, response.getData().getList().size());
        assertEquals(contact, response.getData().getList().get(0).getContact());
    }

    @Test
    void testSave() {
        // 准备测试数据
        EnquiryBean enquiry = new EnquiryBean();
        enquiry.setContact("John");
        enquiry.setTelOrMail("john@example.com");
        enquiry.setStatus(0);

        // 模拟请求IP
        when(request.getHeader(any())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // 模拟服务行为
        when(enquiryService.save(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageEnquiryController.save(request, enquiry);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(enquiryService, times(1)).save(any());
    }

    @Test
    void testUpdateStatus() {
        // 准备测试数据
        Long id = 1L;
        Integer status = 1;

        // 模拟服务行为
        when(enquiryService.updateById(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageEnquiryController.updateStatus(request, id, status);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(id.toString(), response.getData());
        verify(enquiryService, times(1)).updateById(any());
    }

    @Test
    void testRemoveList() {
        // 准备测试数据
        List<Long> idArray = Arrays.asList(1L, 2L);

        // 模拟服务行为
        when(enquiryService.removeByIds(any())).thenReturn(true);

        // 执行测试
        BaseResponse<String> response = pageEnquiryController.removeList(request, idArray);

        // 验证结果
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("1,2", response.getData());
        verify(enquiryService, times(1)).removeByIds(idArray);
    }

    @Test
    void testGetClientIp() {
        // 测试 X-Forwarded-For
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");
        assertEquals("192.168.1.1", pageEnquiryController.getClientIp(request));

        // 测试 Proxy-Client-IP
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.2");
        assertEquals("192.168.1.2", pageEnquiryController.getClientIp(request));

        // 测试 RemoteAddr
        when(request.getHeader(any())).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        assertEquals("127.0.0.1", pageEnquiryController.getClientIp(request));
    }
}