package com.sparta.ch4.delivery.company.application.service;

import com.sparta.ch4.delivery.company.application.dto.ProductDto;
import com.sparta.ch4.delivery.company.domain.exception.ApplicationException;
import com.sparta.ch4.delivery.company.domain.model.Company;
import com.sparta.ch4.delivery.company.domain.model.Product;
import com.sparta.ch4.delivery.company.domain.service.CompanyDomainService;
import com.sparta.ch4.delivery.company.domain.service.ProductDomainService;
import com.sparta.ch4.delivery.company.domain.type.ProductQuantity;
import com.sparta.ch4.delivery.company.domain.type.ProductSearchType;
import com.sparta.ch4.delivery.company.infrastructure.client.HubClient;
import com.sparta.ch4.delivery.company.infrastructure.client.UserClient;
import com.sparta.ch4.delivery.company.infrastructure.client.response.HubResponse;
import com.sparta.ch4.delivery.company.infrastructure.client.response.UserResponse;
import com.sparta.ch4.delivery.company.presentation.response.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.sparta.ch4.delivery.company.domain.exception.ErrorCode.ACCESS_DENIED;
import static com.sparta.ch4.delivery.company.domain.exception.ErrorCode.HUB_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductDomainService productDomainService;
    private final CompanyDomainService companyDomainService;
    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public ProductDto createProduct(ProductDto productDto, String userId) {
        //업체 검증
        Company company = companyDomainService.getCompanyById(productDto.companyId());
        //허브 존재 및 데이터 검증
        if (!hubClient.getHub(productDto.hubId()).getData().id().equals(company.getHubId())) {
            throw new ApplicationException(HUB_NOT_FOUND);
        }
        if (!checkAuthForManaging(userId, company.getId(), productDto.hubId())) {
            throw new ApplicationException(ACCESS_DENIED);
        }
        return ProductDto.from(productDomainService.createProduct(productDto, company));
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        return ProductDto.from(productDomainService.getProductById(productId));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(UUID companyId, UUID hubId, ProductSearchType searchType, String searchValue, Pageable pageable) {
        return productDomainService.getAllProducts(companyId, hubId, searchType, searchValue, pageable).map(ProductDto::from);
    }


    @Transactional
    public ProductDto updateProduct(UUID productId, ProductDto updatingDto, String userId) {
        Product product = productDomainService.getProductById(productId);
        // companyId 가 업데이트 될 때 존재하는 업체인지 확인
        Company company = companyDomainService.getCompanyById(updatingDto.companyId());

        //유저 권한 검증
        if (!checkAuthForManaging(userId, product.getCompany().getId(), product.getId())) {
            throw new ApplicationException(ACCESS_DENIED);
        }

        return ProductDto.from(productDomainService.updateProduct(product, company , updatingDto));
    }

    @Transactional
    public void deleteProduct(UUID productId, String userId) {
        Product product = productDomainService.getProductById(productId);
        //유저 권한 검증
        if (!checkAuthForManaging(userId, product.getCompany().getId(), product.getId())) {
            throw new ApplicationException(ACCESS_DENIED);
        }

        productDomainService.deleteProduct(product, userId);
    }

    // 수량 점검은 order 쪽에서 권한 관리
    @Transactional
    public void updateProductQuantity(UUID productId, Integer quantity, ProductQuantity upAndDown) {
        productDomainService.updateProductQuantity(productId, quantity, upAndDown);
    }

    private boolean checkAuthForManaging(String userId, UUID productCompanyId, UUID productHubId) {
        //- **허브 관리자**: 자신의 허브에 소속된 상품만 관리 가능
        //- **허브 업체**: 자신의 업체의 상품만 생성 및 수정 가능
        //- **기타 사용자**: 읽기와 검색만 가능
        CommonResponse<UserResponse> userApiRes = userClient.getUser(Long.parseLong(userId));
        UserResponse user = userApiRes.getData();
        return switch (user.role()) {
            case MASTER -> true;
            case HUB_MANAGER -> user.hubId().equals(productHubId);
            case HUB_COMPANY -> user.companyId().equals(productCompanyId);
            case HUB_DELIVERY -> false;
        };
    }
}