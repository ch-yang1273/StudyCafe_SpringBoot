package asc.portfolio.ascSb.order.dto;

import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.OrderType;
import lombok.Getter;

@Getter
public class OrderRequest {

    private OrderStatus orderStatus;
    private Long cafeId;
    private OrderType orderType; //todo 이거 줘야하는 값이 너무 복잡함
    private int price;
    private String receiptId;
    private String productLabel;
}
