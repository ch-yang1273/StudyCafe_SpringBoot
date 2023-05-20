package asc.portfolio.ascSb.product.domain;


import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PRODUCT")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long id;

    @Column(name = "CAFE_ID")
    private Long cafeId;

    @Column(name ="USER_ID")
    private Long userId; // 구매자 id, customerId가 더 어울리겠다.

    @Enumerated(EnumType.STRING)
    @Column(name ="STATUE")
    private ProductStatus status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private ProductType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "LABEL", unique = true)
    private String label;

    @Builder
    public Product(Long cafeId, Long userId, ProductStatus status, ProductType type,
                   String description, Integer price, String label)
    {
        this.cafeId = cafeId;
        this.userId = userId;
        this.status = status;
        this.type = type;
        this.description = description;
        this.price = price;
        this.label = label;
    }

    public void cancelProduct() {
        this.status = ProductStatus.CANCEL;
    }

}
