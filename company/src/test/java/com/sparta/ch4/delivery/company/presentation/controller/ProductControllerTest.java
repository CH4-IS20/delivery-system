package com.sparta.ch4.delivery.company.presentation.controller;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.application.service.ProductService;
import com.sparta.ch4.delivery.company.domain.type.CompanyType;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private ProductDto productDto;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()) // Pageable 처리
                .build();

        productDto = ProductDto.builder()
                .id(UUID.randomUUID())
                .companyId(UUID.randomUUID())
                .companyName("Test Company")
                .companyType(CompanyType.RECIPIENT)
                .companyAddress("Test Address")
                .companyHubId(UUID.randomUUID())
                .companyCreatedAt(LocalDateTime.now())
                .hubId(UUID.randomUUID())
                .name("Test Product")
                .quantity(100)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void getProduct_returnsProductResponse() throws Exception {
        UUID productId = UUID.randomUUID();
        when(productService.getProductById(productId)).thenReturn(productDto);

        mockMvc.perform(get("/api/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.id").value(productDto.id().toString()))
                .andExpect(jsonPath("$.data.company.id").value(productDto.companyId().toString()))
                .andExpect(jsonPath("$.data.company.name").value(productDto.companyName()))
                .andExpect(jsonPath("$.data.name").value(productDto.name()))
                .andExpect(jsonPath("$.data.quantity").value(productDto.quantity()))
                .andExpect(jsonPath("$.data.createdAt").exists());
    }

    @Test
    public void getAllProducts_searchByName_returnsProductResponses() throws Exception {
        ProductSearchType searchType = ProductSearchType.NAME;
        String searchValue = "Test"; // -> Test Product

        List<ProductDto> productDtos = List.of(productDto);
        Page<ProductDto> productDtoPage = new PageImpl<>(productDtos,Pageable.ofSize(10), productDtos.size());

        when(productService.getAllProducts(eq(null),eq(null), eq(searchType), eq(searchValue), any(Pageable.class)))
                .thenReturn(productDtoPage);

        mockMvc.perform(get("/api/products")
                        .param("searchType", searchType.name())
                        .param("searchValue", searchValue)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.content[0].id").value(productDto.id().toString()))
                .andExpect(jsonPath("$.data.content[0].company.id").value(productDto.companyId().toString()))
                .andExpect(jsonPath("$.data.content[0].company.name").value(productDto.companyName()))
                .andExpect(jsonPath("$.data.content[0].name").value(productDto.name()))
                .andExpect(jsonPath("$.data.content[0].quantity").value(productDto.quantity()))
                .andExpect(jsonPath("$.data.content[0].createdAt").exists())
                .andExpect(jsonPath("$.data.totalElements").value(productDtos.size()))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

}