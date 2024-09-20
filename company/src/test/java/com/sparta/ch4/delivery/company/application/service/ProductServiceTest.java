package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.service.CompanyDomainService;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import com.sparta.ch4.delivery.company.domain.type.UserRole;
import com.sparta.ch4.delivery.company.infrastructure.client.HubClient;
import com.sparta.ch4.delivery.company.infrastructure.client.UserClient;
import com.sparta.ch4.delivery.company.infrastructure.client.response.HubResponse;
import com.sparta.ch4.delivery.company.infrastructure.client.response.UserResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDomainService productDomainService;
    @Mock
    private CompanyDomainService companyDomainService;
    @Mock
    private HubClient hubClient;
    @Mock
    private UserClient userClient;

    private ProductDto productDto;
    private Product product;
    private UUID productId;
    private UUID companyId;
    private Long userId;
    private Company company;
    private UUID hubId;
    private Pageable pageable;
    private CommonResponse<UserResponse> mockUserResponse;
    private CommonResponse<HubResponse> mockHubClientResponse;


    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        hubId = UUID.randomUUID();
        userId = 1L;
        pageable = Pageable.ofSize(10);
        productDto = ProductDto.builder()
                .id(productId)
                .companyId(companyId)
                .companyHubId(hubId)
                .hubId(hubId)
                .name("Test Product")
                .quantity(100)
                .build();
        company = Company.builder().id(companyId).hubId(hubId).build();
        product = Product.builder().id(productId)
                .company(company)
                .hubId(hubId).name("Test Product")
                .quantity(100).build();
        // 기본 생성자 취소하기
        product.setIsDeleted(null);
        mockUserResponse = new CommonResponse<>(200,"성공");
        UserResponse mockUser = new UserResponse(userId,hubId, companyId,
                "TestUser", "TestEmail", UserRole.MASTER,"slackId");
        mockUserResponse.setData(mockUser);
        mockHubClientResponse = new CommonResponse<>(200, "성공");
        HubResponse mockHubResponse = new HubResponse(hubId,"hubName","hubAddress",35,130);
        mockHubClientResponse.setData(mockHubResponse);
    }

    @Test
    void testCreateProduct() {
        // Given : mock service data , 유저 , 허브 클라이언트
        // When
        when(companyDomainService.getCompanyById(companyId)).thenReturn(company);
        when(hubClient.getHub(hubId)).thenReturn(mockHubClientResponse); // 허브 검증
        when(userClient.getUser(anyLong())).thenReturn(mockUserResponse); // 유저 검증
        when(productDomainService.createProduct(any(ProductDto.class), any(Company.class))).thenReturn(product);

        ProductDto result = productService.createProduct(productDto, userId.toString());

        // Assert
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productDomainService, times(1)).createProduct(productDto, company);
        verify(hubClient, times(1)).getHub(hubId); // 허브 검증
        verify(userClient, times(1)).getUser(userId); // 유저 검증
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(productDomainService.getProductById(any(UUID.class))).thenReturn(product);

        // Act
        ProductDto result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productDomainService, times(1)).getProductById(productId);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productDomainService.getAllProducts(any(UUID.class), any(UUID.class), any(ProductSearchType.class), anyString(), any(Pageable.class)))
                .thenReturn(productPage);

        // Act
        Page<ProductDto> result = productService.getAllProducts(companyId, hubId, ProductSearchType.NAME, "Test", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productDomainService, times(1))
                .getAllProducts(companyId, hubId, ProductSearchType.NAME, "Test", pageable);
    }

    @Test
    void testUpdateProduct() {
        // Given
        // When
        when(productDomainService.getProductById(productId)).thenReturn(product);
        when(companyDomainService.getCompanyById(companyId)).thenReturn(company);
        when(userClient.getUser(anyLong())).thenReturn(mockUserResponse);
        when(productDomainService.updateProduct(any(Product.class), any(Company.class), any(ProductDto.class)))
                .thenReturn(product);

        // Act
        ProductDto result = productService.updateProduct(productId, productDto, userId.toString());

        // Then
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productDomainService, times(1)).updateProduct(product, company, productDto);
        verify(userClient, times(1)).getUser(userId);
    }

    @Test
    void testDeleteProduct() {
        //When
        when(productDomainService.getProductById(any(UUID.class))).thenReturn(product);
        when(userClient.getUser(anyLong())).thenReturn(mockUserResponse);
        // Act
        productService.deleteProduct(productId, userId.toString());

        // Assert
        verify(userClient, times(1)).getUser(userId);
        verify(productDomainService, times(1)).deleteProduct(product, userId.toString());
    }
}