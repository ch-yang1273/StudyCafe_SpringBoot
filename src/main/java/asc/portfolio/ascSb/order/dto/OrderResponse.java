package asc.portfolio.ascSb.order.dto;

import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.OrderType;
import asc.portfolio.ascSb.order.domain.Orders;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    private final Long id;
    private final OrderStatus status;
    private final OrderType type;
    private final String typeString;
    private final Integer price;
    private final String label;
    private final LocalDateTime createDate;

    @Builder
    public OrderResponse(Long id, OrderStatus status, OrderType type, String typeString,
                         Integer price, String label,
                         LocalDateTime createDate) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.typeString = typeString;
        this.price = price;
        this.label = label;
        this.createDate = createDate;
    }

    public OrderResponse(Orders orders) {
        this.id = orders.getId();
        this.status = orders.getStatus();
        this.type = orders.getOrderType();
        this.typeString = orders.getOrderType().getValue();
        this.price = orders.getPrice();
        this.label = orders.getProductLabel();
        this.createDate = orders.getCreateDate();
    }
}
