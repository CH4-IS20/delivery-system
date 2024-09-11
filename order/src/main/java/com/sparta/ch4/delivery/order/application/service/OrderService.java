package com.sparta.ch4.delivery.order.application.service;

import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.service.DeliveryDomainService;
import com.sparta.ch4.delivery.order.domain.service.DeliveryHistoryDomainService;
import com.sparta.ch4.delivery.order.domain.service.OrderDomainService;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.infrastructure.client.CompanyClient;
import com.sparta.ch4.delivery.order.infrastructure.client.HubRouteClient;
import com.sparta.ch4.delivery.order.infrastructure.client.ProductClient;
import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.CompanyWithUserForOrderResponse;
import com.sparta.ch4.delivery.order.infrastructure.client.response.HubRouteForOrderResponse;
import com.sparta.ch4.delivery.order.infrastructure.client.response.ProductResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderDomainService orderDomainService;
    private final DeliveryDomainService deliveryDomainService;
    private final DeliveryHistoryDomainService deliveryHistoryDomainService;

    private final ProductClient productClient;
    private final CompanyClient companyClient;
    private final HubRouteClient hubRouteClient;

    //TODO : client API response 받을 시 data null 인지 확인하는 검증 필요
    @Transactional
    public OrderDto createOrder(OrderDto dto) {
        //1. 상품 재고 확인 요청
        CommonResponse<ProductResponse> productApiResponse = productClient.getProductById(dto.productId());
        ProductResponse product = productApiResponse.getData();
        if (product.quantity() < dto.quantity()) {
            // TODO: 커스텀 에러 정의
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        // 재고 감소 업데이트
        // TODO: 재고 감소 & 복구 API 만들기
        var updateProductRequest = buildProductUpdateRequestForQuantity(product, product.quantity() - dto.quantity());
        productClient.updateProduct(product.id(), updateProductRequest);

        // 2. 공급,수령 업체 ID를 바탕으로 허브 서비스에 [경로, 배송담당자, 예상시간,예상거리] 요청
        CommonResponse<List<HubRouteForOrderResponse>> hubRouteResponse = hubRouteClient.getHubRouteForOrder(
                dto.supplierId(),
                dto.receiverId()
        );
        List<HubRouteForOrderResponse> hubRoute = hubRouteResponse.getData();

        // 3. 수령업체 정보를 통해 [최종 배송지 및 수령업체 유저 정보] 요청

        CommonResponse<CompanyWithUserForOrderResponse> companyResponse = companyClient.getCompanyInfoForOrder(dto.receiverId());
        CompanyWithUserForOrderResponse company = companyResponse.getData();

        //주문 관련 객체 프로세스 : 주문 생성 -> 배송 생성 -> 배송 기록 생성
        //주문 생성
        Order order = orderDomainService.create(dto.toEntity());

        //배송 생성
        Delivery delivery = deliveryDomainService.create(
                buildDelivery(order, hubRoute ,company)
        );
        //배송 경로 기록 생성
        List<DeliveryHistory> deliveryHistory = deliveryHistoryDomainService.create(
                buildDeliveryHistory(delivery, hubRoute)
        );

        return OrderDto.from(order);
    }

    // 재고 수정 update request
    private ProductUpdateRequest buildProductUpdateRequestForQuantity(ProductResponse product, Integer quantity) {
        return ProductUpdateRequest.builder().companyId(product.company().id())
                .hubId(product.hubId()).name(product.name()).quantity(quantity).build();
    }

    // 배송 객체 생성
    private Delivery buildDelivery(
            Order order, List<HubRouteForOrderResponse> hubRoutes, CompanyWithUserForOrderResponse company
    ) {
        UUID startHubId = hubRoutes.get(0).startHubId();
        UUID endHubId = hubRoutes.get(hubRoutes.size() - 1).destHubId();

        return Delivery.builder()
                .order(order)
                .deliveryAddress(company.address())
                .startHub(startHubId)    //시작 허브
                .endHub(endHubId)    // 최종목적지허브
                .status(DeliveryStatus.HUB_WAITING)
                .recipient(company.recipient())
                .recipientSlack(company.recipientSlack())
                .build();
    }

    //배송 기록 객체 생성
    private List<DeliveryHistory> buildDeliveryHistory(
            Delivery delivery, List<HubRouteForOrderResponse> hubRoutes
    ) {
        List<DeliveryHistory> deliveryHistoryList = new ArrayList<>();
        AtomicInteger sequenceCounter = new AtomicInteger(1);  // 시퀀스 시작 값을 1로 설정
        hubRoutes.forEach(hubRoute ->  {
            var history = DeliveryHistory.builder()
                    .delivery(delivery)
                    .deliveryManagerId(hubRoute.deliveryManagerId())
                    .hubRouteId(hubRoute.hubRouteId())
                    .sequence(sequenceCounter.getAndIncrement())  // 1부터 증가
                    .estimatedDistance(hubRoute.estimatedDistance())
                    .estimatedDuration(hubRoute.estimatedDuration())
                    .actualDistance(null)
                    .actualDuration(null)
                    .status(DeliveryStatus.HUB_WAITING)
                    .build();
            deliveryHistoryList.add(history);
        });
        return deliveryHistoryList;
    }
}
