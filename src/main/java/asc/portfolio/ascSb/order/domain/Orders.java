package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ORDER_TABLE")
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CAFE_ID")
    private Long cafeId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PRODUCT_TYPE")
    private OrderType orderType;

    @Column(name = "ORDER_PRICE")
    private int price;

    @Column(name = "RECEIPT_ID", unique = true)
    private String receiptId; // PG사의 검증을 위한 영수증 id (휘발성)

    @Column(name = "PRODUCT_LABEL", unique = true)
    private String productLabel; // 상품 고유번호

    @Builder
    public Orders(Long id, OrderStatus status, Long userId, Long cafeId, OrderType orderType,
                  int price, String receiptId, String productLabel) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.cafeId = cafeId;
        this.orderType = orderType;
        this.price = price;
        this.receiptId = receiptId;
        this.productLabel = productLabel;
    }

    // 주문 정상적으로 완료
    public void completeOrder() {
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    // 주문 승인 오류
    public void failedToConfirmOrder() {
        this.status = OrderStatus.PAYMENT_ERROR;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }
}
