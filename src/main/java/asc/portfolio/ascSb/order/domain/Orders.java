package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.product.domain.ProductType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ORDERS")
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "USER_ID")
    private OrderStatus status;

    @Column(name = "USER_ID")
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PRODUCT_TYPE")
    private ProductType productType;

    @Column(name = "ORDER_PRICE")
    private Long orderPrice;

    @Column(name = "RECEIPT_ID", unique = true)
    private String receiptId; // PG사의 검증을 위한 영수증 id (휘발성)

    @Column(name = "PRODUCT_LABEL", unique = true)
    private String productLabel; // 상품 고유번호

    @Builder
    private Orders(OrderStatus status, Long userId, ProductType
            productType, Long orderPrice, String receiptId, String productLabel) {
        this.status = status;
        this.userId = userId;
        this.productType = productType;
        this.orderPrice = orderPrice;
        this.receiptId = receiptId;
        this.productLabel = productLabel;
    }

    /**
     * 주문 정상적으로 완료
     */
    public void completeOrder() {
        this.status = OrderStatus.DONE;
    }

    /**
     * 주문 실패
     */
    public void failOrder() {
        this.status = OrderStatus.CANCEL;
    }
}
