package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.commonenum.product.ProductNameType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "O_ID", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderStateType orderStateType;

    @Column(name = "U_I")
    private String userId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "O_PN")
    private ProductNameType orderProductName;

    @Column(name = "O_P")
    private Long orderPrice;

    @Column(unique = true)
    private String receiptOrderId; // PG사의 검증을 위한 영수증 id (휘발성)

    @Column(name = "P_R", unique = true)
    private String productLabel; // 상품 고유번호

    @Builder
    private Orders(OrderStateType orderStateType, String userId, ProductNameType
            orderProductName, Long orderPrice, String receiptOrderId, String productLabel) {
        this.orderStateType = orderStateType;
        this.userId = userId;
        this.orderProductName = orderProductName;
        this.orderPrice = orderPrice;
        this.receiptOrderId = receiptOrderId;
        this.productLabel = productLabel;
    }

    /**
     * 주문 정상적으로 완료
     */
    public void completeOrder() {
        this.orderStateType = OrderStateType.DONE;
    }

    /**
     * 주문 실패
     */
    public void failOrder() {
        this.orderStateType = OrderStateType.CANCEL;
    }
}
