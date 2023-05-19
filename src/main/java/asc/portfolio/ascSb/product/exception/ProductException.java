package asc.portfolio.ascSb.product.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class ProductException extends BusinessException {
    public ProductException(ProductErrorData data) {
        super(data);
    }
}
