package asc.portfolio.ascSb.product.dto;

import asc.portfolio.ascSb.product.domain.ProductType;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private final Long id;
    private final ProductStatus status;
    private final ProductType type;
    private final String productTypeString;
    private final String description;
    private final Integer productPrice;
    private final String productLabel;
    private final LocalDateTime createDate;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.status = product.getStatus();
        this.type = product.getType();
        this.productTypeString = product.getType().getValue();
        this.description = product.getDescription();
        this.productPrice = product.getPrice();
        this.productLabel = product.getLabel();
        this.createDate = product.getCreateDate();
    }
}
