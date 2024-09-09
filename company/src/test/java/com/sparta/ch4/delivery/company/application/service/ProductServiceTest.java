package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
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

    private ProductDto productDto;
    private UUID productId;
    private UUID companyId;
    private UUID hubId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        hubId = UUID.randomUUID();
        pageable = Pageable.ofSize(10);

        productDto = ProductDto.builder()
                .id(productId)
                .companyId(companyId)
                .hubId(hubId)
                .name("Test Product")
                .quantity(100)
                .build();
    }

    @Test
    void testCreateProduct() {
        // Arrange
        when(productDomainService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        // Act
        ProductDto result = productService.createProduct(productDto);

        // Assert
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productDomainService, times(1)).createProduct(productDto);
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(productDomainService.getProductById(any(UUID.class))).thenReturn(productDto);

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
        Page<ProductDto> productPage = new PageImpl<>(Collections.singletonList(productDto));
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
        // Arrange
        when(productDomainService.updateProduct(any(UUID.class), any(ProductDto.class))).thenReturn(productDto);

        // Act
        ProductDto result = productService.updateProduct(productId, productDto);

        // Assert
        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productDomainService, times(1)).updateProduct(productId, productDto);
    }

    @Test
    void testDeleteProduct() {
        // Act
        productService.deleteProduct(productId, "testUser");

        // Assert
        verify(productDomainService, times(1)).deleteProduct(productId, "testUser");
    }
}