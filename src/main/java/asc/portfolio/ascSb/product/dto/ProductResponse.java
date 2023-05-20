package asc.portfolio.ascSb.product.dto;

import asc.portfolio.ascSb.product.domain.ProductType;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ProductResponse {

    private ProductStatus productState;
    private ProductType productType;
    private String productTypeString;
    private String description;
    private Integer productPrice;
    private String productLabel;
    private LocalDateTime createDate;

    public ProductResponse(Product product) {
        this.productState = product.getStatus();
        this.productType = product.getType();
        this.description = product.getDescription();
        this.productPrice = product.getPrice();
        this.productLabel = product.getLabel();
        this.productTypeString = product.getType().getValue();
        this.createDate = product.getCreateDate();
    }
}
