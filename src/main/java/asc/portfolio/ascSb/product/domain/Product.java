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
    @Column(name = "P_ID", nullable = false)
    private Long id;

    @JoinColumn(name = "C_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_ID")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private ProductStateType productState;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "O_PN")
    private ProductNameType productNameType;

    @Column(name = "DE")
    private String description;

    @Column(name = "P_P")
    private Integer productPrice;

    @Column(unique = true)
    private String productLabel;

    @Builder
    public Product(Cafe cafe, User user, ProductStateType productState, ProductNameType productNameType,
                   String description, Integer productPrice, String productLabel)
    {
        this.cafe = cafe;
        this.user = user;
        this.productState = productState;
        this.productNameType = productNameType;
        this.description = description;
        this.productPrice = productPrice;
        this.productLabel = productLabel;
    }

    public void saleProduct() {
        this.productState = ProductStateType.SALE;
    }

    public void cancelProduct() {
        this.productState = ProductStateType.CANCEL;
    }

}
