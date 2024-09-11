package com.sparta.ch4.delivery.order.application.service;

import com.sparta.ch4.delivery.order.application.dto.OrderDto;
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
import com.sparta.ch4.delivery.order.infrastructure.client.ProductClient;
import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.CompanyResponse;
import com.sparta.ch4.delivery.order.infrastructure.client.response.HubRouteForOrderResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
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

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void registerEventListener() {
        circuitBreakerRegistry.circuitBreaker("orderService").getEventPublisher()
                .onStateTransition(event -> log.info("#######CircuitBreaker State Transition: {}", event)) // 상태 전환 이벤트 리스너
                .onFailureRateExceeded(event -> log.info("#######CircuitBreaker Failure Rate Exceeded: {}", event)) // 실패율 초과 이벤트 리스너
                .onCallNotPermitted(event -> log.info("#######CircuitBreaker Call Not Permitted: {}", event)) // 호출 차단 이벤트 리스너
                .onError(event -> log.info("#######CircuitBreaker Error: {}", event)); // 오류 발생 이벤트 리스너
    }

    //TODO : client API response 받을 시 data null 인지 확인하는 검증 필요
    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackForDecreasedQuantity")
    public OrderDto createOrder(OrderDto dto) {
        try {
            //1. 상품 재고 확인 요청 및 재고 업데이트
            CallDecreaseProductQuantity(dto);

            // 2. 공급,수령 업체 ID를 바탕으로 허브 서비스에 [경로, 배송담당자, 예상시간,예상거리] 요청
            CommonResponse<List<HubRouteForOrderResponse>> hubRouteResponse = hubRouteClient.getHubRouteForOrder(
                    dto.supplierId(),
                    dto.receiverId()
            );
            List<HubRouteForOrderResponse> hubRoute = hubRouteResponse.getData();

            // 3. 수령업체 정보를 통해 [최종 배송지 및 수령업체 유저 정보] 요청

            CommonResponse<CompanyResponse> companyResponse = companyClient.getCompany(dto.receiverId());
            if (companyResponse.getData() == null) {
                // TODO: 커스텀 에러 정의
                throw new IllegalArgumentException("ID 에 해당하는 업체를 찾을 수 없습니다.");
            }
            ;

            //주문 관련 객체 프로세스 : 주문 생성 -> 배송 생성 -> 배송 기록 생성
            //주문 생성
            Order order = orderDomainService.create(dto.toEntity());

            //배송 생성
            Delivery delivery = deliveryDomainService.create(
                    buildDelivery(order, hubRoute, dto)
            );
            //배송 경로 기록 생성
            List<DeliveryHistory> deliveryHistory = deliveryHistoryDomainService.create(
                    buildDeliveryHistory(delivery, hubRoute)
            );

            return OrderDto.from(order);
        }catch(RuntimeException e){
            // fallback
            log.error("주문 생성 실패: ", e);
            throw e;
        }

    }


    @Transactional
    public Page<OrderDto> getAllOrders(OrderSearchType searchType, String searchValue, Pageable pageable) {
        return orderDomainService.getAll(searchType, searchValue, pageable).map(OrderDto::from);
    }

    //TODO: 구현
    @Transactional
    public OrderDto updateOrder(UUID orderId, OrderDto dto) {
        // 무엇을 업데이트 할 수 있나?
        // ->  수량

        return OrderDto.builder().build();
    }


    // 재고 감소 api
    public void CallDecreaseProductQuantity(OrderDto dto) {
        productClient.updateQuantity(dto.productId(), ProductQuantityUpdateRequest.from(dto.quantity(), ProductQuantity.DOWN));
    }

    // 재고 복구 api 호출
    public OrderDto fallbackForDecreasedQuantity(OrderDto dto, Throwable throwable) {
        log.error("주문 생성 실패로 인해 fallback 호출됨: ", throwable);
        // 보상 로직: 수량 증가
        try {
            productClient.updateQuantity(dto.productId(),ProductQuantityUpdateRequest.from(dto.quantity(), ProductQuantity.UP));
        } catch (Exception ex) {
            log.error("상품 수량 증가 실패: ", ex);
        }
        return  dto;
    }



    // 배송 객체 생성
    private Delivery buildDelivery(
            Order order, List<HubRouteForOrderResponse> hubRoutes, OrderDto orderDto
    ) {
        UUID startHubId = hubRoutes.get(0).startHubId();
        UUID endHubId = hubRoutes.get(hubRoutes.size() - 1).destHubId();

        Delivery delivery = Delivery.builder()
                .order(order)
                .deliveryAddress(orderDto.receiptAddress())
                .startHub(startHubId)    //시작 허브
                .endHub(endHubId)    // 최종목적지허브
                .status(DeliveryStatus.HUB_WAITING)
                .recipient(orderDto.recipientName())
                .recipientSlack(orderDto.recipientSlack())
                .build();
        delivery.setCreatedBy(orderDto.createdBy());
        return delivery;
    }

    //배송 기록 객체 생성
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
