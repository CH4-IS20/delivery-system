package com.sparta.ch4.delivery.order.domain.type;



public enum OrderSearchType {
     USER_ID,  //주문자 ID
     SUPPLIER_ID,  //요청 업체 ID (공급 업체)
     RECEIVER_ID,	 // 수령 업체 ID
     PRODUCT_ID,     // 주문 상품 ID
     QUANTITY,	 //	주문 수량
     ORDER_DATE,  // 주문 일자
     STATUS;	 // 	주문 상태 (pending, completed, canceled)
}
