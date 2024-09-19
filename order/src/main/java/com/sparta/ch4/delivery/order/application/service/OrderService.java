package com.sparta.ch4.delivery.order.application.service;

import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.application.dto.OrderCreateDto;
import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.exception.ApplicationException;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.service.DeliveryDomainService;
import com.sparta.ch4.delivery.order.domain.service.DeliveryHistoryDomainService;
import com.sparta.ch4.delivery.order.domain.service.OrderDomainService;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.domain.type.ProductQuantity;
import com.sparta.ch4.delivery.order.infrastructure.client.CompanyClient;
import com.sparta.ch4.delivery.order.infrastructure.client.HubRouteClient;
import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.CompanyResponse;
import com.sparta.ch4.delivery.order.infrastructure.client.response.HubRouteForOrderResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sparta.ch4.delivery.order.domain.exception.ErrorCode.COMPANY_CLIENT_ERROR;
import static com.sparta.ch4.delivery.order.domain.exception.ErrorCode.PRODUCT_INVALID_ARGUMENT;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderDomainService orderDomainService;
    private final DeliveryDomainService deliveryDomainService;
    private final DeliveryHistoryDomainService deliveryHistoryDomainService;

    private final CompanyClient companyClient;
    private final HubRouteClient hubRouteClient;


    @Transactional
    public OrderDto createOrder(OrderCreateDto orderCreatDto) {
        try {
            //1. 상품 재고 확인 요청 및 재고 업데이트
            companyClient.updateQuantity(orderCreatDto.productId(), ProductQuantityUpdateRequest.from(orderCreatDto.quantity(), ProductQuantity.DOWN));

            // 2. 공급,수령 업체 ID를 바탕으로 허브 서비스에 [경로, 배송담당자, 예상시간,예상거리] 요청
            CommonResponse<List<HubRouteForOrderResponse>> hubRouteResponse = hubRouteClient.getHubRouteForOrder(
                    orderCreatDto.supplierId(),
                    orderCreatDto.receiverId(),
                    orderCreatDto.createdBy()
            );
            List<HubRouteForOrderResponse> hubRoute = hubRouteResponse.getData();

            // 3. 수령업체 정보 검증
            CommonResponse<CompanyResponse> companyResponse = companyClient.getCompany(orderCreatDto.receiverId());
            if (companyResponse.getData() == null) {
                throw new ApplicationException(COMPANY_CLIENT_ERROR);
            }

            //주문 관련 객체 프로세스 : 주문 생성 -> 배송 생성 -> 배송 기록 생성
            //주문 생성
            Order order = orderDomainService.create(orderCreatDto.toEntity());

            //배송 생성
            DeliveryDto deliveryDto = buildDeliveryDto(hubRoute, orderCreatDto);
            Delivery delivery = deliveryDomainService.create(
                    deliveryDto.toEntity(order, orderCreatDto.createdBy())
            );
            //배송 경로 기록 생성
            deliveryHistoryDomainService.create(
                    buildDeliveryHistory(delivery, hubRoute)
            );

            return OrderDto.from(order);
        } catch (ApplicationException e) {
            // 감소된 재고 복구 api call
            companyClient.updateQuantity(orderCreatDto.productId(), ProductQuantityUpdateRequest.from(orderCreatDto.quantity(), ProductQuantity.UP));
            log.error("주문 생성 실패: ", e);
            throw e;
        }

    }


    @Transactional
    public Page<OrderDto> getAllOrders(OrderSearchType searchType, String searchValue, Pageable pageable) {
        return orderDomainService.getAll(searchType, searchValue, pageable).map(OrderDto::from);
    }

    @Transactional
    public OrderDto getOrder(UUID orderId) {
        return OrderDto.from(orderDomainService.getOrderById(orderId));
    }

    @Transactional
    public OrderDto updateOrder(UUID orderId, OrderDto dto) {
        // 무엇을 업데이트 할 수 있나?
        // ->  일단 수량만...  / quantity만 업데이트 하는 메소드로 분리할 수 있겠다.
        // 1. order 조회
        Order order = orderDomainService.getOrderById(orderId);
        if (!order.getProductId().equals(dto.productId())) {
            throw new ApplicationException(PRODUCT_INVALID_ARGUMENT);
        }
        // 2. product 재고 콜 -> 줄었는지 늘었는지 체크
        if (order.getQuantity() < dto.quantity()) { //기존 주문보다 늘었다면 차이만큼만 재고 Down 요청
            companyClient.updateQuantity(dto.productId(), ProductQuantityUpdateRequest.from(
                    dto.quantity() - order.getQuantity()
                    , ProductQuantity.DOWN));
        } else if (order.getQuantity() > dto.quantity()) { // 기존 주문보다 줄었다면 차이만큼 재고 UP 요청
            companyClient.updateQuantity(dto.productId(), ProductQuantityUpdateRequest.from(
                    order.getQuantity() - dto.quantity()
                    , ProductQuantity.UP));
        }
        return OrderDto.from(orderDomainService.update(order, dto));
    }

    @Transactional
    public void deleteOrder(UUID orderId, String deletedBy) {
        orderDomainService.delete(orderId, deletedBy);
    }



    // 배송 Dto 객체 생성
    private DeliveryDto buildDeliveryDto(
            List<HubRouteForOrderResponse> hubRoutes, OrderCreateDto orderCreateDto
    ) {
        UUID startHubId = hubRoutes.get(0).startHubId();
        UUID endHubId = hubRoutes.get(hubRoutes.size() - 1).destHubId();

        return DeliveryDto.builder()
                .deliveryAddress(orderCreateDto.receiptAddress())
                .startHub(startHubId)
                .endHub(endHubId)
                .status(DeliveryStatus.HUB_WAITING)
                .recipient(orderCreateDto.recipientName())
                .recipientSlack(orderCreateDto.recipientSlack())
                .build();
    }

    //배송 기록 엔티티 객체 생성
    private List<DeliveryHistory> buildDeliveryHistory(
            Delivery delivery, List<HubRouteForOrderResponse> hubRoutes
    ) {
        List<DeliveryHistory> deliveryHistoryList = new ArrayList<>();
        AtomicInteger sequenceCounter = new AtomicInteger(1);  // 시퀀스 시작 값을 1로 설정
        hubRoutes.forEach(hubRoute -> {
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
            history.setCreatedBy(delivery.getCreatedBy());
            deliveryHistoryList.add(history);
        });
        return deliveryHistoryList;
    }
}
