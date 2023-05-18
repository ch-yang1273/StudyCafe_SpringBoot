package asc.portfolio.ascSb.order.dto;

import asc.portfolio.ascSb.product.domain.ProductType;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrderStateType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDto {

    private OrderStateType orderStateType;
    private String userId;
    private ProductType orderProduct;
    private Long orderPrice;
    private String receiptOrderId;
    private String productLabel;

    @Builder
    public OrderDto(OrderStateType orderStateType, String userId, ProductType orderProduct, Long orderPrice,
                    String receiptOrderId, String productLabel) {
        this.orderStateType = orderStateType;
        this.userId = userId;
        this.orderProduct = orderProduct;
        this.orderPrice = orderPrice;
        this.receiptOrderId = receiptOrderId;
        this.productLabel = productLabel;
    }

    public Orders toEntity() {
        return Orders.builder()
                .orderStateType(orderStateType)
                .userId(userId)
                .productLabel(productLabel)
                .productType(orderProduct)
                .orderPrice(orderPrice)
                .receiptOrderId(receiptOrderId)
                .build();
    }
}
