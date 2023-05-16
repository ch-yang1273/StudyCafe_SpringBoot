package asc.portfolio.ascSb.product.dto;

import asc.portfolio.ascSb.product.domain.ProductNameType;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductStateType;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ProductResponse {

    private ProductStateType productState;
    private ProductNameType productNameType;
    private String productNameTypeString;
    private String description;
    private Integer productPrice;
    private String productLabel;
    private LocalDateTime createDate;

    public ProductResponse(Product product) {
        this.productState = product.getProductState();
        this.productNameType = product.getProductNameType();
        this.description = product.getDescription();
        this.productPrice = product.getProductPrice();
        this.productLabel = product.getProductLabel();
        this.productNameTypeString = product.getProductNameType().getValue();
        this.createDate = product.getCreateDate();
    }
}
