package asc.portfolio.ascSb.product.dto;

import asc.portfolio.ascSb.product.domain.ProductNameType;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductStateType;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDto {

    private Cafe cafe;
    private User user;
    private ProductStateType productState;
    private ProductNameType productNameType;

    private String productNameTypeString;
    private String description;
    private Integer productPrice;
    private String productLabel;


    @Builder
    public ProductDto(Cafe cafe, User user, ProductStateType productState, ProductNameType productNameType,
                      String description, Integer productPrice, String productLabel) {
        this.cafe = cafe;
        this.user = user;
        this.productState = productState;
        this.productNameType = productNameType;
        this.description = description;
        this.productPrice = productPrice;
        this.productLabel = productLabel;
        this.productNameTypeString = productNameType.name();
    }

    public Product toEntity() {
        return Product.builder()
                .cafe(cafe)
                .user(user)
                .productState(productState)
                .productNameType(productNameType)
                .description(description)
                .productPrice(productPrice)
                .productLabel(productLabel)
                .build();
    }
}
