package asc.portfolio.ascSb.order.dto;

import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.product.domain.ProductType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRequest {

    private OrderStatus orderStatus;
    private String userId;
    private ProductType productType; //todo 이거 줘야하는 값이 너무 복잡함
    private Long orderPrice;
    private String receiptId;
    private String productLabel;
}
