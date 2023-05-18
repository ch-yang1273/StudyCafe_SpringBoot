package asc.portfolio.ascSb.product.dto;

import asc.portfolio.ascSb.product.domain.ProductType;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductStatus;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDto {

    private Cafe cafe;
    private User user;
    private ProductStatus productState;
    private ProductType productType;

    private String productNameTypeString;
    private String description;
    private Integer productPrice;
    private String productLabel;


    @Builder
    public ProductDto(Cafe cafe, User user, ProductStatus productState, ProductType productType,
                      String description, Integer productPrice, String productLabel) {
        this.cafe = cafe;
        this.user = user;
        this.productState = productState;
        this.productType = productType;
        this.description = description;
        this.productPrice = productPrice;
        this.productLabel = productLabel;
        this.productNameTypeString = productType.name();
    }

    public Product toEntity() {
        return Product.builder()
                .cafe(cafe)
                .user(user)
                .productStatus(productState)
                .productType(productType)
                .description(description)
                .price(productPrice)
                .label(productLabel)
                .build();
    }
}
