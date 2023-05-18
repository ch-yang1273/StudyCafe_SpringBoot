package asc.portfolio.ascSb.product.domain;


import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JoinColumn(name = "CAFE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_ID")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name ="STATUE")
    private ProductStatus productState;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private ProductType productType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "LABEL", unique = true)
    private String label;

    @Builder
    public Product(Cafe cafe, User user, ProductStatus productStatus, ProductType productType,
                   String description, Integer price, String label)
    {
        this.cafe = cafe;
        this.user = user;
        this.productState = productStatus;
        this.productType = productType;
        this.description = description;
        this.price = price;
        this.label = label;
    }

    public void cancelProduct() {
        this.productState = ProductStatus.CANCEL;
    }

}
