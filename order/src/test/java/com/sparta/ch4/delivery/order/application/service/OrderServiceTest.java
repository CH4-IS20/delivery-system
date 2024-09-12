package com.sparta.ch4.delivery.order.application.service;

import com.sparta.ch4.delivery.order.application.dto.DeliveryDto;
import com.sparta.ch4.delivery.order.application.dto.OrderCreateDto;
import com.sparta.ch4.delivery.order.application.dto.OrderDto;
import com.sparta.ch4.delivery.order.domain.model.Delivery;
import com.sparta.ch4.delivery.order.domain.model.DeliveryHistory;
import com.sparta.ch4.delivery.order.domain.model.Order;
import com.sparta.ch4.delivery.order.domain.service.DeliveryDomainService;
import com.sparta.ch4.delivery.order.domain.service.DeliveryHistoryDomainService;
import com.sparta.ch4.delivery.order.domain.service.OrderDomainService;
import com.sparta.ch4.delivery.order.domain.type.CompanyType;
import com.sparta.ch4.delivery.order.domain.type.DeliveryStatus;
import com.sparta.ch4.delivery.order.domain.type.OrderSearchType;
import com.sparta.ch4.delivery.order.domain.type.OrderStatus;
import com.sparta.ch4.delivery.order.infrastructure.client.CompanyClient;
import com.sparta.ch4.delivery.order.infrastructure.client.HubRouteClient;
import com.sparta.ch4.delivery.order.infrastructure.client.ProductClient;
import com.sparta.ch4.delivery.order.infrastructure.client.request.ProductQuantityUpdateRequest;
import com.sparta.ch4.delivery.order.infrastructure.client.response.CompanyResponse;
import com.sparta.ch4.delivery.order.infrastructure.client.response.HubRouteForOrderResponse;
import com.sparta.ch4.delivery.order.presentation.response.CommonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTest.class);
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private DeliveryDomainService deliveryDomainService;

    @Mock
    private DeliveryHistoryDomainService deliveryHistoryDomainService;

    @Mock
    private ProductClient productClient;

    @Mock
    private CompanyClient companyClient;

    @Mock
    private HubRouteClient hubRouteClient;

    private OrderDto orderDto;
    private OrderCreateDto orderCreateDto;
    private Order order;
    private Delivery delivery;
    private DeliveryDto deliveryDto;
    private DeliveryHistory deliveryHistory;
    private UUID orderId;
    private UUID productId;
    private UUID companyId;
    private UUID supplierId;
    private UUID deliveryId;
    private UUID startHubId;
    private UUID endHubId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        productId = UUID.randomUUID();
        companyId = UUID.randomUUID();
        supplierId = UUID.randomUUID();
        deliveryId = UUID.randomUUID();
        startHubId = UUID.randomUUID();
        endHubId = UUID.randomUUID();
        pageable = Pageable.ofSize(10);

        deliveryDto = DeliveryDto.builder().id(deliveryId).status(DeliveryStatus.HUB_WAITING)
                .startHub(startHubId).endHub(endHubId).deliveryAddress("123 Test St")
                .recipient("John Doe").recipientSlack("john.doe.slack").isDeleted(false).build();

        orderCreateDto = OrderCreateDto.builder().userId(1L).supplierId(supplierId).receiverId(companyId)
                .productId(productId).quantity(10).status(OrderStatus.PENDING).build();

        orderDto = OrderDto.builder().id(orderId).userId(1L).supplierId(supplierId).receiverId(companyId)
                .productId(productId).deliveryDto(deliveryDto).quantity(10)
                .status(OrderStatus.PENDING).isDeleted(false).build();

        order = Order.builder().id(orderId).userId(1L).supplierId(supplierId).receiverId(companyId).productId(productId)
                .quantity(10).status(OrderStatus.PENDING).build();

        delivery = Delivery.builder().id(deliveryId).order(order).status(DeliveryStatus.HUB_WAITING)
                .startHub(startHubId).endHub(endHubId).deliveryAddress("123 Test St")
                .recipient("John Doe").recipientSlack("john.doe.slack").build();

        order.setDelivery(delivery);
        deliveryHistory = DeliveryHistory.builder().id(UUID.randomUUID()).delivery(delivery)
                .deliveryManagerId(UUID.randomUUID()).hubRouteId(UUID.randomUUID()).sequence(1)
                .estimatedDistance(10.0f).estimatedDuration(60).status(DeliveryStatus.HUB_WAITING).build();


    }

    @Test
    void testCreateOrder_재고감소_성공() {
        //Given  연관된 domain 로직 & API call
        when(orderDomainService.create(any(Order.class))).thenReturn(order);
        when(hubRouteClient.getHubRouteForOrder(any(UUID.class), any(UUID.class)))
                .thenReturn(CommonResponse.success(List.of(
                        new HubRouteForOrderResponse(
                                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()
                                , 10.0f, 60
                        )
                )));
        when(companyClient.getCompany(any(UUID.class)))
                .thenReturn(CommonResponse.success(new CompanyResponse(
                        companyId, UUID.randomUUID(), "Company Name", CompanyType.MANUFACTURER
                        , "123 Company St", LocalDateTime.now()
                )));
        when(deliveryDomainService.create(any(Delivery.class))).thenReturn(delivery);
        when(deliveryHistoryDomainService.create(anyList())).thenReturn(List.of(deliveryHistory));
        doNothing().when(productClient).updateQuantity(any(UUID.class), any(ProductQuantityUpdateRequest.class));
        //When  서비스 메소드 수행
        OrderDto result = orderService.createOrder(orderCreateDto);
        //Then  Assert
        assertNotNull(result);
        assertEquals(orderDto, result);
        verify(orderDomainService, times(1)).create(any(Order.class));
        verify(hubRouteClient, times(1)).getHubRouteForOrder(any(UUID.class), any(UUID.class));
        verify(companyClient, times(1)).getCompany(any(UUID.class));
        verify(deliveryDomainService, times(1)).create(any(Delivery.class));
        verify(deliveryHistoryDomainService, times(1)).create(anyList());
        verify(productClient, times(1)).updateQuantity(any(UUID.class)
                , any(ProductQuantityUpdateRequest.class)
        );
    }


    @Test
    void testGetAllOrders() {
        // Arrange
        Page<OrderDto> orderPage = new PageImpl<>(List.of(orderDto));
        when(orderDomainService.getAll(any(OrderSearchType.class), any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(order)));

        // Act
        Page<OrderDto> result = orderService.getAllOrders(OrderSearchType.STATUS, "PENDING", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderDomainService, times(1)).getAll(any(OrderSearchType.class), any(String.class)
                , any(Pageable.class)
        );
    }


}